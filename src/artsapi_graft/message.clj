(ns artsapi-graft.message
  (:import [javax.mail.internet
            MimeMessage
            MimeMultipart
            InternetAddress
            MimeUtility]
           [javax.mail Message Message$RecipientType]))

(defn get-header
  "Returns the header corresponding to the string passed in, if it exists.
   This will be returned as a string or vector of strings."
  [msg header]
  (let [header-field (.getHeader msg header)]
    (when (not= nil header-field)
      (if (> (count header-field) 1)
        (vec header-field)
        (str (get header-field 0))))))

(defn subject
  [msg]
  (.getSubject msg))

(defn sender
  "Returns a InternetAddress object from the sender's metadata"
  [msg]
  (let [sender-email (.getFrom msg)]
    (get sender-email 0)))

(defn sender->str
  "Returns a string of the sender's email"
  [msg]
  (.getAddress (sender msg)))

(defn to
  "Returns a InternetAddress object of all recipients' addresses."
  [msg]
  (let [recipients (.getRecipients msg Message$RecipientType/TO)]
    (if (> (count recipients) 1)
      (set recipients)
      (get recipients 0))))

(defn to->str
  "Returns a vector of strings of all recipients' email addresses."
  [msg]
  (let [recipients (to msg)]
    (if (seq? recipients)
      (map #(.getAddress (get % 0)) recipients)
      (.getAddress recipients))))

(defn sent-date
  "Returns a clojure date time instance corresponding to the send date"
  [msg]
  (.getSentDate msg))

(defn sent-date->str
  "Returns a string of the date and time an email was sent"
  [msg]
  (str (sent-date msg)))

(defn received-date
  "Returns a clojure date time instance corresponding to the received date."
  [msg]
  (.getReceivedDate msg))

(defn received-date->str
  "Returns a string of the date ad time an email was received."
  [msg]
  (str (received-date msg)))

(defn cc
  [msg]
  msg)

(defn ->msg
  "Return a map of strings representing the message."
  [msg]
  {:from (sender->str msg)
   :to (to->str msg)
   :subject (subject msg)
   :cc ""})

