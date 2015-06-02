(ns artsapi-graft.keywords
  (:require [artsapi-graft.json :refer :all]))

(def keywords->seq
  (-> (->> (read-whole-file "keywords.json")
           (reduce str))
      json->keyword-hash
      ((partial map #(:keyword %)))
      seq))

(defn included-keywords
  [msg]
  (if-not (nil? msg)
    (filter identity
            (map #(re-find (re-pattern (str "\\b" % "\\b")) msg) keywords->seq))))
