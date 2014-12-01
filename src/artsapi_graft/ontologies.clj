(ns artsapi-graft.ontololgies
  (:require [grafter.rdf.ontologies.util :refer :all]))

(def arts (prefixer "http://artsapi.co.uk/def/arts/"))

(def arts:Domain (arts "Domain"))

(def arts:TwitterAccount (arts "TwitterAccount"))

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

(def arts:recipient (arts "recipient"))

(def arts:emailRecipient (arts "emailRecipient"))

(def arts:ccRecipient (arts "ccRecipient"))

(def arts:mentions (arts "mentions"))

