(ns artsapi-graft.store
  (:import [javax.mail.internet
            MimeMessage
            MimeMultipart
            InternetAddress
            MimeUtility]))

;; NB for some reason you may have to (.saveChanges msg) to populate fields

(defn subject
  [msg]
  (.getSubject msg))

(defn from
  "Returns a InternetAddress object from the sender's metadata"
  [msg]
  (let [sender (.getFrom msg)]
    (get sender 0)))

(defn from->string
  "Returns a string of the sender's email"
  [msg]
  (.getAddress (from msg)))

(defn to)

(defn to->string)

(defn sent-date
  "Returns a clojure date time instance corresponding to the send date"
  [msg]
  (.getSentDate msg))

(defn sent-date->string
  "Returns a string of the date and time an email was sent"
  [msg]
  (str (sent-date msg)))

(defn cc)
