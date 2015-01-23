(ns artsapi-graft.templates.email
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

# this is a pipeline to generate a linked data ontology from the SIC2007 hierarchy.
# the input file is the six2007summaryofstructur_tcm77-223506.xls file in the doc directory.

# the column structure is a bit odd, it corresponds to:
# A: section B: section label C: division D: division label E: group F: group label G: class H: class label I: sub class J: sub class label

(def sic-graph-uri)

(defn catch-nil->s
  "The s function, with the ability to represent nil as a string when used with arity of 1."
  [str]
  (if (nil? str)
    "nil"
    (s str)))

(def sanitize-sic-code
  [sic-code]
  (clojure.string/replace sic-code #"" ""))

(def sic-ontology-template
  (graph-fn [[section section-label division division-label group group-label class class-label sub-class sub-class-label]]
            (graph sic-graph-uri
                   [(cond () ())
                    [rdf:a (cond
                            (nil? section) sic:section
                            (nil? division) sic:division
                            (nil? group) sic:group
                            (nil? class) sic:class
                            (nil? sub-class) sic:sub-class)]
                    [rdfs:label ]
                    [sic:code ]])
