(ns artsapi-graft.templates.sic
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [artsapi-graft.core :refer [write-to-ttl strike-nils]]))

;; this is a pipeline to generate a linked data ontology from the SIC2007 hierarchy.
;; the input file is the six2007summaryofstructur_tcm77-223506.xls file in the doc directory.

;; the column structure is a bit odd, it corresponds to:
;; A: section B: section label C: division D: division label E: group F: group label G: class H: class label I: sub class J: sub class label

;; we need a graph uri and a prefixer, plus some ontology temrs and we don't want to include all the arts api boilerplate

(def sic-graph-uri "http://publishmydata.com/graph/sic")

;; ontology

(def sic (prefixer "http://publishmydata.com/def/sic/"))

(def sic:section (sic "section"))

(def sic:division (sic "division"))

(def sic:group (sic "group"))

(def sic:class (sic "class"))

(def sic:sub-class (sic "subclass"))

(def sic:code (sic "code"))

;; resource prefixer

(def sic-resource (prefixer "http://publishmydata.com/id/sic/"))

(defn catch-nil->s
  "The s function, with the ability to represent nil as a string when used with arity of 1."
  [str]
  (if (nil? str)
    "nil"
    (s str)))

(def not-nil? (complement nil?))

(defn uriify-sic-code
  [sic-code]
  (-> sic-code
      (clojure.string/replace #"\.| |\/" "")
      sic-resource))

(def sic-ontology-template
  (graph-fn [[section section-label division division-label group group-label class class-label sub-class sub-class-label]]
            (graph sic-graph-uri
                   [(cond
                     (not-nil? section) (uriify-sic-code section)
                     (not-nil? division) (uriify-sic-code division)
                     (not-nil? group) (uriify-sic-code group)
                     (not-nil? class) (uriify-sic-code class)
                     (not-nil? sub-class) (uriify-sic-code sub-class)
                     :else "nil")
                    [rdf:a (cond
                            (not-nil? section) sic:section
                            (not-nil? division) sic:division
                            (not-nil? group) sic:group
                            (not-nil? class) sic:class
                            (not-nil? sub-class) sic:sub-class
                            :else "nil")]
                    [rdfs:label (cond
                                 (not-nil? section) (s section-label)
                                 (not-nil? division) (s division-label)
                                 (not-nil? group) (s group-label)
                                 (not-nil? class) (s class-label)
                                 (not-nil? sub-class) (s sub-class-label)
                                 :else "nil")]
                    [sic:code (cond
                               (not-nil? section) (s (clojure.string/replace section #" " ""))
                               (not-nil? division) (s division)
                               (not-nil? group) (s group)
                               (not-nil? class) (s class)
                               (not-nil? sub-class) (s sub-class)
                               :else "nil")]])))

(defn run-sic-pipeline
  [input destination]
  (write-to-ttl
   (sic-ontology-template (drop-rows (first (open-all-datasets input))
                                     1))
   destination))
