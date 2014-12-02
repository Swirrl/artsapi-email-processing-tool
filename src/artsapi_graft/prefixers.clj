(ns artsapi-graft.prefixers
  (:require [grafter.rdf.ontologies.util :refer :all]
            [artsapi-graft.ontologies :refer :all]))

(def base-graph (prefixer (arts "graph/")))

(def organisation-graph-uri (prefixer (base-graph "organisation/")))

(def person-graph-uri (prefixer (base-graph "person/")))

(def email-graph-uri (prefixer (base-graph "email/")))

(def organisation-uri)

(def person-uri)

(def email-uri)
