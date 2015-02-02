(ns artsapi-graft.linkedin
  (:require [clojure.java.io :as jio]
            [grafter.tabular :refer :all]
            [artsapi-graft.io :as io]))

(defn filter-csv
  "A helper function that looks in a directory
   and only returns .csv files that match the regex passed in."
  [dir regex]
  (->> (io/open-file-directory dir)
       (filter #(re-find regex %))))

(defn read-csv
  [file]
  (read-dataset file :format :csv :separator \tab :encoding "UTF-16"))

(defn get-linkedin-email-addresses
  "Looks inside a directory, grabs the file named Email Addresses.csv
   and returns the email addresses inside, ready to key against for other LinkedIn datasets"
  [path-to-directory]
  (let [file (first (filter-csv path-to-directory #"Email Addresses\.csv\z"))]
    (-> (read-csv file)
        (drop-rows 1)
        (columns [0]))))
