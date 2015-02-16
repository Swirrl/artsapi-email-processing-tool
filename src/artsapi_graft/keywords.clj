(ns artsapi-graft.keywords
  (:require [artsapi-graft.json :refer :all]))

(def keywords->lazy-seq
  (-> (->> (read-whole-file "resources/keywords.json")
           (reduce str))
      json->keyword-hash
      ((partial map #(:keyword %)))
      lazy-seq))
