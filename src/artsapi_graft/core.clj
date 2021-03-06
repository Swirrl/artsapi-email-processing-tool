(ns artsapi-graft.core
  (:require [clojure.java.io :as javaio]
            [grafter.rdf.protocols :as pr]
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
                (in-quad? "http://data.artsapi.co.uk/id/organisations/nil-example-com" quad)
                (in-quad? "http://data.artsapi.co.uk/id/domains/nil-example-com" quad)
                (in-quad? "http://data.artsapi.co.uk/id/people/nil" quad)
                (in-quad? "http://data.artsapi.co.uk/id/people/undisclosed-recipients:;" quad)
                (in-quad? "nil.example.com" quad)
                (in-quad? "http://nil.example.com" quad)))
          quads))

;; if your files are small, you can un-comment to get detailed logging
;; at the expense of lazy evaluation

(defn load-slice
  [batch repo] 
  (println (str "> Adding "
                (count batch)
                " quads..."))
  (try
    (pr/add repo batch)
    (println "✔ Added. ")
    
    (catch org.openrdf.repository.RepositoryException e
      (println (str "✘ Unable to add: "
                    e
                    " - skipping. ")))))

(defn load->db
  [quads query-url update-url]
  
  (let [repo (sparql-repo query-url update-url)
        validated-quads (strike-nils quads)
        batched-quads (partition-all 10000 validated-quads)]

    (doseq [batch batched-quads]
      (load-slice batch repo))))

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

;; usage: (def array (map str (walk "path-to-directory-here" #".*\.mbox")))
(defn walk [dirpath pattern]
  "A cribbed example of recursively finding files and filtering.
   Use as in the comment above, the hand the seq off to the seq->db function below"
  (doall (filter #(re-matches pattern (.getName %))
                 (file-seq (javaio/file dirpath)))))

(defn seq->db
  [seq query-url update-url]
  (map #(-> (email->quads %)
            (load->db query-url update-url))
       seq))

(defn -main
  ([path output]
     (-> (dispatcher path)
         (write-to-file output)))
  ([path query-url update-url]
     (-> (dispatcher path)
         (load->db query-url update-url))
     (println "-> Load complete"))
  ([path query-url update-url do-not-convert]
     (-> (email->quads-excluding-keywords path)
         (load->db query-url update-url))
     (println "-> Load complete")))

