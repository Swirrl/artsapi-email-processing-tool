(ns artsapi-graft.store
  (:require [artsapi-graft.message :refer :all])
  (:import [net.fortuna.mstor]
           [java.util Properties]
           [java.io FileInputStream File]
           [javax.mail.internet MimeMessage MimeMultipart InternetAddress MimeUtility]
           [javax.mail Session Store Folder Message URLName Flags Flags$Flag  Message$RecipientType]))

;; three useful methods lifted from clojure-mail

(defmacro with-store
  "Takes a store which has been connected, and binds to to *store* within the
   scope of the form.
   **Usage:**
   user> (with-store (gmail-store \"username@gmail.com\" \"password\")
   (read-messages :inbox 5))
   ;=> "
  [s & body]
  `(binding [*store* ~s]
     ~@body))

(defn as-properties
  [m]
  (let [p (Properties.)]
    (doseq [[k v] m]
      (.setProperty p (str k) (str v)))
    p))

(defn file->message
  "read a downloaded mail message in the same format
   as you would find on the mail server. This can
   be used to read saved messages from text files
   and for parsing fixtures in tests etc"
  [path-to-message]
  (let [props (Session/getDefaultInstance (Properties.))
        msg (FileInputStream. (File. path-to-message))]
    (MimeMessage. props msg)))

;; basically a re-implementation of clojure-mail's store without the
;; assumption that we want to connect to an imap store

;; has some other utility methods that wrap parts of the javamail and
;; mstor APIs to allow for working with mbox files in read only and
;; read write mode.

(defn store
  "A store models a message store in mbox format. Pass this function
   the absolute path to the mbox archive you want to work with."
  [path]
  (let [p (as-properties
           [["mail.store.protocol" "mstor"]
            ["mstor.cache.disabled" "true"]
            ["mstor.mbox.metadataStrategy" "none"]])]
    (doto
        (.getStore (Session/getDefaultInstance p)
                   (URLName. (str "mstor:" path)))
      (.connect))))

(defn open-read-only-folder
  "Open a folder in read only mode."
  [folder]
  (.open folder Folder/READ_ONLY))

(defn close-read-only-folder
  "Close a read only folder. As it is read only, we don't expunge deleted messages."
  [folder]
  (.close folder false))

(defn open-folder
  "Open a folder in Read/Write mode."
  [folder]
  (.open folder Folder/READ_WRITE))

(defn close-folder
  "Close a folder. We also expunge any messages that have been deleted.
   This will throw an exception for a read only folder."
  [folder]
  (.close folder true))

(defn get-msg-count-for
  "Return the number of messages in a folder. The folder must be open."
  [folder]
  (.getMessageCount folder))

(defn get-messages
  "Get all messages from the specified folder. This will return a seq of msg hash objects."
  [folder]
  (try 
    (open-read-only-folder folder)
    (let [ret (map ->msg (.getMessages folder))]
      (close-read-only-folder folder)
      ret)
    (catch javax.mail.FolderNotFoundException e
      nil)))

(defn get-folders [store folder-names]
  (->> folder-names
       (map (fn [name]
              (if (= :default name)
                (.getDefaultFolder store)
                (.getFolder store name))))
       (filter identity)))

(defn get-all-messages
  "Gets all messages from a store as a seq."
  [store folders]
  (mapcat get-messages (get-folders store folders)))

