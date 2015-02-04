(ns artsapi-graft.core
  (:require [grafter.rdf.protocols :as pr]
            [artsapi-graft.pipeline :refer :all]
            [grafter.rdf :as rdf]
            [grafter.rdf.io :as io])
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
  (remove (fn [quad] (or (in-quad? "nil" quad)
                        (in-quad? "http://artsapi.co.uk/id/organisations/nil-example-com" quad)
                        (in-quad? "http://artsapi.co.uk/id/domains/nil-example-com" quad)
                        (in-quad? "http://artsapi.co.uk/id/people/nil" quad)
                        (in-quad? "http://artsapi.co.uk/id/people/undisclosed-recipients:;" quad)
                        (in-quad? "nil.example.com" quad)
                        (in-quad? "http://nil.example.com" quad)))
          quads))

(defn write-to-ttl
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
    (re-find #"mbox\z" path) (email->quads path)))

(defn -main [path output]
  (-> (dispatcher path)
      (write-to-ttl output)))

