(ns artsapi-graft.store
  (:import [net.fortuna.mstor]
           [java.util Properties]
           [java.io FileInputStream File]
           [javax.mail.internet MimeMessage MimeMultipart]
           [javax.mail Session Store Folder Message URLName]))

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

(defn store
  "A store models a message store in mbox format. Pass this function
   the absolute path to the mbox archive you want to work with."
  [path]
  (let [p (as-properties
           [["mail.store.protocol" "mstor"]])]
    (doto
        (.getStore (Session/getDefaultInstance p)
                   (URLName. (str "mstor:" path)))
      (.connect))))
