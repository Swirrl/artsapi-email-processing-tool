(ns artsapi-graft.emails
  (:require [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defn generate-email-uri
  "Pass this an email map and have a unique graph uri returned for the email in question."
  [email])

(defn generate-mailto-uri
  "Pass this a string containing an email address and have a mailto string returned."
  [address]
  (str "mailto:" address))
