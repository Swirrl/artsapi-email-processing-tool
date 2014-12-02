(ns artsapi-graft.prefixers
  (:require [grafter.rdf.ontologies.util :refer :all]
            [artsapi-graft.ontologies :refer :all]))

(def base-graph (prefixer (arts "graph/")))

(def organisation-graph-uri (prefixer (base-graph "organisations")))

(def project-graph-uri (prefixer (base-graph "projects")))

(def domain-graph-uri (prefixer (base-graph "domains")))

(def person-graph-uri (prefixer (base-graph "people")))

(def email-graph-uri (prefixer (base-graph "emails")))

(defn ->slug
  [id-string])

(defn resource-uri
  "Generate a resource uri. For example, these outputs should be expected:
   ['people' 'Jeff Lebowski] ;=>
   ['email' 'jeff@example.com'] ;=>
   ['domain' 'swirrl.com'] ;=> "
  [graph id-string]
  (str (prefixer (base-graph "graph/id/"))
       (->slug id-string)))

(defn user-account
  "Generate a uri for a user's account. These are nested under the resource uri for the person
   that owns the account instance.
   Example: ['Jeff Lebowski' :email] ;=> domain/graph/people/jeff-lebowski/accounts/email"
  [user-slug account])

