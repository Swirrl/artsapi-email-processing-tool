(ns artsapi-graft.message
  (:import [javax.mail.internet
            MimeMessage
            MimeMultipart
            InternetAddress
            MimeUtility]))

;; NB for some reason you may have to (.saveChanges msg) to populate fields

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

(defn received-date
  "Returns a clojure date time instance corresponding to the received date."
  [msg]
  (.getReceivedDate msg))

(defn received-date->string
  "Returns a string of the date ad time an email was received."
  [msg]
  (str (received-date msg)))

(defn cc)
