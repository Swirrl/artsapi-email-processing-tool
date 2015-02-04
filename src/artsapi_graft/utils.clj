(ns artsapi-graft.utils
  (:require [grafter.tabular :refer :all]))


;; utility functions from sns-graft

(defn seq-of-hashes->Dataset [hashes]
  "Converts seq of hashes (rows) into a Dataset, assumes every row has the same keys"
  (if-let [key-cols (keys (first hashes))]
    (make-dataset (map vals hashes) key-cols)
    (make-dataset [])))

(defn expand-rows [dataset row-expander-fn]
  (->> dataset
       :rows
       (mapcat row-expander-fn)
       seq-of-hashes->Dataset))

;; snippets from Rick

(defn merge-all-rows [emails-dataset]
  (fn [row]
    (for [email-row (:rows emails-dataset)]
      (merge row email-row))))

(defpipe mypipeline [dataset emails-ds]
  (let [emails-dataset (read-dataset emails-ds)]
    (-> (read-dataset dataset)
        (expand-rows (merge-all-rows emails-datset)))))
