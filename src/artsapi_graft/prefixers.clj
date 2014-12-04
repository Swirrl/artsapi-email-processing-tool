(ns artsapi-graft.prefixers
  (:require [grafter.rdf.ontologies.util :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [digest :refer :all]))

;; graph uris

(def base-graph (prefixer (arts-domain "graph/")))

(def organisation-graph-uri (base-graph "organisations"))

(def project-graph-uri (base-graph "projects"))

(def domain-graph-uri (base-graph "domains"))

(def person-graph-uri (base-graph "people"))

(def email-graph-uri (base-graph "emails"))

(def tweet-graph-uri (base-graph "tweets"))

(def twitter-account-graph-uri (base-graph "twitter-accounts"))

(def email-account-graph-uri (base-graph "email-accounts"))

;; resource uris

(def base-resource-uri (prefixer (arts-domain "id/")))

(defn ->slug
  [id-string]
  (-> id-string
      (clojure.string/trim)
      (clojure.string/lower-case)
      (clojure.string/replace #"\.|@| " "-")))

(defn resource-uri
  "Generate a resource uri. For example, these outputs should be expected:
   ['people' 'jeff@example.com'] ;=> /id/people/jeff-example-com
   ['domain' 'swirrl.com'] ;=> /id/swirrl-com
   ['twitter-account' 'jeff_lebowski'] ;=> /id/twitter-accounts/jeff_lebowski"
  [graph id-string]
  (str (base-resource-uri graph) "/"
       (->slug id-string)))

(defn email-uri
  [from sent-at subject]
  (let [mashed-str (str from sent-at subject)]
    (resource-uri "emails" (digest/md5 mashed-str))))
