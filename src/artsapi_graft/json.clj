(ns artsapi-graft.json
  (:require [clojure.java.io :as io]
            [cheshire.core :refer :all]))

(defn open-file-directory
  [path-to-directory]
  (->> (.listFiles (io/file path-to-directory))
       (map #(.getAbsolutePath %))))

(defn read-whole-file
  [file-path]
  (with-open [reader (io/reader file-path)]
    (doall (line-seq reader))))

(defn read-file-from-line-two
  "This is a utility function; we know that in Twitter dumps the JSON starts on line 2."
  [file-path]
  (->> (read-whole-file file-path)
      (drop 1)
      (reduce str)))

(defn json->keyword-hash
  "Returns a lazy seq of hashes that correspond to the input json."
  [json]
  (parse-string json #(keyword %)))
