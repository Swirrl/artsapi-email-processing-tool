(ns artsapi-graft.emails
  (:require [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defn generate-mailto-uri
  "Pass this a string containing an email address and have a mailto string returned."
  [address]
  (str "mailto:" address))
