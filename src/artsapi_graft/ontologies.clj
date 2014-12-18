(ns artsapi-graft.ontologies
  (:require [grafter.rdf.ontologies.util :refer :all]))

(def arts-domain (prefixer "http://artsapi.co.uk/"))

(def arts (prefixer "http://artsapi.co.uk/def/arts/"))

(def arts:Domain (arts "Domain"))

(def arts:TwitterAccount (arts "TwitterAccount"))

(def arts:EmailAccount (arts "EmailAccount"))

(def arts:Message (arts "Message"))

(def arts:Post (arts "Post"))

(def arts:Tweet (arts "Tweet"))

(def arts:Email (arts "Email"))

(def arts:ownsDomain (arts "ownsDomain"))

(def arts:hasDomain (arts "hasDomain"))

(def arts:worksOn (arts "worksOn"))

(def arts:workedOn (arts "workedOn"))

(def arts:sender (arts "sender"))

(def arts:tweetSender (arts "tweetSender"))

(def arts:emailSender (arts "emailSender"))

(def arts:sentAt (arts "sentAt"))

(def arts:subject (arts "subject"))

(def arts:content (arts "content"))

(def arts:emailSubject (arts "emailSubject"))

(def arts:recipient (arts "recipient"))

(def arts:emailRecipient (arts "emailRecipient"))

(def arts:ccRecipient (arts "ccRecipient"))

(def arts:mentions (arts "mentions"))

;; catch missing vcard org and foaf terms

(def vcard:hasEmail "http://www.w3.org/2006/vcard/ns#hasEmail")

(def foaf:made "http://xmlns.com/foaf/0.1/made")

(def foaf:mbox "http://xmlns.com/foaf/0.1/mbox")

(def foaf:accountName "http://xmlns.com/foaf/0.1/accountName")

(def org:memberOf "http://www.w3.org/ns/org#memberOf")

(def org:hasMember "http://www.w3.org/ns/org#hasMember")
