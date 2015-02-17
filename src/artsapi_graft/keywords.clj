(ns artsapi-graft.keywords
  (:require [artsapi-graft.json :refer :all]))

(def keywords->seq
  (-> (->> (read-whole-file "resources/keywords.json")
           (reduce str))
      json->keyword-hash
      ((partial map #(:keyword %)))
      seq))

(defn included-keywords
  [msg]
  (filter identity
          (map #(re-find (re-pattern %) msg) keywords->seq)))
