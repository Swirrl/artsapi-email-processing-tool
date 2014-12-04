(ns artsapi-graft.message
  (:require [artsapi-graft.prefixers :refer [email-uri]])
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

(defn get-subject
  [msg]
  (.getSubject msg))

(defn get-sender
  "Returns a InternetAddress object from the sender's metadata."
  [msg]
  (let [sender-email (.getFrom msg)]
    (get sender-email 0)))

(defn get-to
  "Returns a InternetAddress object of all recipients' addresses, or a set of them."
  [msg]
  (into #{} (.getRecipients msg Message$RecipientType/TO)))

(defn get-sent-date
  "Returns a clojure date time instance corresponding to the send date."
  [msg]
  (.getSentDate msg))

(defn sent-date->str
  "Returns a string of the date and time an email was sent."
  [msg]
  (str (get-sent-date msg)))

(defn get-received-date
  "Returns a clojure date time instance corresponding to the received date."
  [msg]
  (.getReceivedDate msg))

(defn received-date->str
  "Returns a string of the date and time an email was received."
  [msg]
  (str (get-received-date msg)))

(defn address->str
  "Returns a string generated by calling getAddress on an InternetAddress object."
  [address]
  (.getAddress address))

(defn get-personal
  "Returns a string generated by calling getPersonal on an InternetAddress object."
  [address]
  (.getPersonal address))

(defn get-domain
  "Returns a fully qualified domain name extracted from an email address."
  [address]
  (let [addr (address->str address)]
    (subs (re-find #"@.+" addr) 1)))

(defn get-cc
  "Returns an InternetAddress object of CC recipients, or a set containing them."
  [msg]
  (into #{} (.getRecipients msg Message$RecipientType/CC)))

(defn get-content
  "Get message contents"
  [msg]
  (.getContent msg))

(defn ->msg
  "Return a map of strings representing the message."
  [msg]
  (let [sender (get-sender msg)
        from-str (address->str sender)
        date (sent-date->str msg)
        subj (get-subject msg)
        email-resource-uri (email-uri from-str date subj)]
    {:from from-str
     :from-personal (get-personal sender)
     :from-domain (get-domain sender)
     :sent-date date
     :received-date (received-date->str msg)
     :to (map #(-> {:email-uri email-resource-uri
                    :personal (get-personal %)
                    :email (address->str %)
                    :domain (get-domain %)})
              (get-to msg))
     :subject subj
     :cc (map #(-> {:email-uri email-resource-uri
                    :personal (get-personal %)
                    :email (address->str %)
                    :domain (get-domain %)})
              (get-cc msg))
     :content (get-content msg)}))

