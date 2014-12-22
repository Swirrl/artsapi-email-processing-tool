(ns artsapi-graft.core
  (:require [grafter.rdf.protocols :as pr]
            [artsapi-graft.pipeline :refer :all]
            [grafter.rdf.sesame :as ses]
            [artsapi-graft.store :as st :refer [store]]))

(defn strike-nils
  [quads]
  (remove (fn [quad] (= "nil" (pr/object quad))) quads))

;; TODO: validate quads
(defn write-to-ttl
  [quads destination]
  (let [validated-quads (strike-nils quads)]
    (pr/add (ses/rdf-serializer destination) validated-quads)))

(defn init-store [path]
  (st/store path))

(defn pipeline-dispatcher
  "Check the format of the input path and work out what to do.
   This is very unscientific at present and will need updating.

   tweets is the folder that the js files live in when you get a
   Twitter dump.
   mbox is a plaintext mbox file and the default convention so
   far as I know; it comes from a folder with the .mbox suffix."
  [path]
  (cond
    (re-find #"tweets\z" path) (twitter-pipeline path)
    (re-find #"mbox\z" path) (email-pipeline (init-store path))))

(defn -main [path output]
  (-> (pipeline-dispatcher path)
      (write-to-ttl output)))
