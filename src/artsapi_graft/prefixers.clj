(ns artsapi-graft.prefixers
  (:require [grafter.rdf.ontologies.util :refer :all]
            [artsapi-graft.ontologies :refer :all]))

;; graph uris

(def base-graph (prefixer (arts "graph/")))

(def organisation-graph-uri (prefixer (base-graph "organisations")))

(def project-graph-uri (prefixer (base-graph "projects")))

(def domain-graph-uri (prefixer (base-graph "domains")))

(def person-graph-uri (prefixer (base-graph "people")))

(def email-graph-uri (prefixer (base-graph "emails")))

(def tweet-graph-uri (prefixer (base-graph "tweets")))

(def twitter-account-graph-uri (prefixer (base-graph "twitter-accounts")))

(def email-account-graph-uri (prefixer (base-graph "email-accounts")))

;; resource uris

(def base-resource-uri (prefixer (arts "id/")))

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
  (str (prefixer (base-resource-uri graph)) "/"
       (->slug id-string)))

