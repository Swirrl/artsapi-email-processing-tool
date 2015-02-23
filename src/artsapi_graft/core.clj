(ns artsapi-graft.core
  (:require [grafter.rdf.protocols :as pr]
            [artsapi-graft.quad-converters :refer :all]
            [grafter.rdf :as rdf]
            [grafter.rdf.io :as io]
            [grafter.rdf.repository :refer [sparql-repo]])
  (:gen-class))

;; Next we need to get a list of common hosting domains and filter out
;; the orgs before they get to this point (or maybe here), e.g. we
;; don't want an org with a domain of gmail.com or hotmail.com

(defn in-quad?
  [value quad]
  (some true?
        (map #(= value %)
             [(pr/subject quad)
              (pr/predicate quad)
              (pr/object quad)
              (pr/context quad)])))

(defn strike-nils
  [quads]
  (remove (fn [quad]
            (or (in-quad? "nil" quad)
                (in-quad? "http://artsapi.co.uk/id/organisations/nil-example-com" quad)
                (in-quad? "http://artsapi.co.uk/id/domains/nil-example-com" quad)
                (in-quad? "http://artsapi.co.uk/id/people/nil" quad)
                (in-quad? "http://artsapi.co.uk/id/people/undisclosed-recipients:;" quad)
                (in-quad? "nil.example.com" quad)
                (in-quad? "http://nil.example.com" quad)))
          quads))

(defn load-slice
  [quad-slice repo]
  (pr/add repo (first quad-slice))
  (second quad-slice))

(defn load->db
  [quads query-url update-url]
  (let [repo (sparql-repo query-url update-url)
        validated-quads (strike-nils quads)]
    (loop [quads validated-quads]
      (let [quad-slice (split-at 10000 quads)]
        (if-not (empty? (first quad-slice))
          (println "-> Load complete")
          (recur (load-slice quad-slice repo)))))))

(defn write-to-file
  [quads destination]
  (let [validated-quads (strike-nils quads)]
    (pr/add (io/rdf-serializer destination) validated-quads)))

(defn dispatcher
  "Check the format of the input path and work out what to do.
   This is very unscientific at present and will need updating.

   tweets is the folder that the js files live in when you get a
   Twitter dump.
   mbox is a plaintext mbox file and the default convention so
   far as I know; it comes from a folder with the .mbox suffix."
  [path]
  (cond
    (re-find #"tweets\z" path) (twitter->quads path)
    (re-find #"mbox\z" path) (email->quads path)
    (re-find #"LinkedInDataExport" path) (linkedin->quads path)))

(defn -main
  ([path output]
     (-> (dispatcher path)
         (write-to-file output)))
  ([path query-url update-url]
     (-> (dispatcher path)
         (load->db query-url update-url))))

