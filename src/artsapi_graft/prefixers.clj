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

(def linkedin-account-graph-uri (base-graph "linkedin-accounts"))

(def linkedin-recommendation-graph-uri (base-graph "recommendations"))

(def linkedin-endorsement-graph-uri (base-graph "endorsements"))

(def linkedin-skill-graph-uri (base-graph "linkedin-skills"))

(def keywords-graph-uri (base-graph "keywords"))

;; resource uris

(def base-resource-uri (prefixer (arts-domain "id/")))

(defn trim-trailing-hyphens
  [slug]
  (if (re-find #"-$" slug)
    (clojure.string/replace slug #"-$" "")))

(defn sanitize
  [string]
  (clojure.string/replace string #"'|\"|\\" "" ))

(defn ->slug
  [id-string]
  (-> id-string
      (clojure.string/trim)
      (clojure.string/lower-case)
      (clojure.string/replace #"\.|@| " "-")
      sanitize))

(defn resource-uri
  "Generate a resource uri. For example, these outputs should be expected:
   ['people' 'jeff@example.com'] ;=> /id/people/jeff-example-com
   ['domains' 'swirrl.com'] ;=> /id/swirrl-com
   ['twitter-account' 'jeff_lebowski'] ;=> /id/twitter-accounts/jeff_lebowski"
  [graph id-string]
  (str (base-resource-uri graph) "/"
       (->slug id-string)))

(defn linkedin-hash
  [graph timestamp first-name last-name]
  (->> (str timestamp first-name last-name)
       digest/md5
       (resource-uri graph)))

(defn email-uri
  [from sent-at subject]
  (let [mashed-str (str from sent-at subject)]
    (resource-uri "emails" (digest/md5 mashed-str))))

(defn tweet-uri
  [from sent-at content]
  (let [mashed-str (str from sent-at content)]
    (resource-uri "tweets" (digest/md5 mashed-str))))
