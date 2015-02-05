(ns artsapi-graft.linkedin
  (:require [clojure.java.io :as jio]
            [grafter.tabular :refer :all]
            [grafter.sequences :refer :all]
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

(defn get-linkedin-email-ds
  "Looks inside a directory, grabs the file named Email Addresses.csv
   and returns the email addresses inside, ready to key against for other LinkedIn datasets"
  [path-to-directory]
  (let [file (first (filter-csv path-to-directory
                                #"Email Addresses\.csv\z"))]
    (-> (read-csv file)
        (drop-rows 1))))

(defn get-linkedin-email-addresses
  "Returns email addresses as a lazy seq"
  [path-to-directory]
  (-> (get-linkedin-email-ds path-to-directory)
      (columns [0])
      :rows
      ((partial map #(% "a")))
      lazy-seq))

(defn get-linkedin-primary-email
  "Returns the primary email address as a string"
  [path-to-directory]
  (-> (get-linkedin-email-ds path-to-directory)
      (grep "Yes")
      (columns [0])
      :rows
      ((partial map #(% "a")))
      first))

(defn read-and-add-primary
  "Performs actions common to all LinkedIn pipelines - reading the file
   and dropping row 1 - before adding the primary as a new column
   to be used later in templating"
  [file primary]
  (-> (read-csv file)
      (drop-rows 1)
      (add-column :owner-email primary)))
