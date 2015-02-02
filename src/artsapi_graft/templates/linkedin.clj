(ns artsapi-graft.templates.linkedin
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))


(defgraft connections-template
  (graph-fn [[]]))

(defgraft endorsements-template
  (graph-fn [[]]))

(defgraft recommendations-template
  (graph-fn [[]]))

(defgraft skills-template
  (graph-fn [[]]))

(defgraft ad-targeting-template
  (graph-fn [[]]))
