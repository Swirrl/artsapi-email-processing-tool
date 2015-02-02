(ns artsapi-graft.io
  (:require [clojure.java.io :as io]))

(defn open-file-directory
  [path-to-directory]
  (->> (.listFiles (io/file path-to-directory))
       (map #(.getAbsolutePath %))))
