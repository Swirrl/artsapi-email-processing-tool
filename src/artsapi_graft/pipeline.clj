(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.linkedin :refer :all]
            [artsapi-graft.templates.linkedin :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]))

(defn get-sender-email-ds
  [messages]
  (make-dataset messages [:from :from-personal :from-domain :sent-date :subject]))

(defpipe linkedin-connections-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Connections\.csv\z"))]
    (read-and-add-primary file primary)))

(defpipe linkedin-endorsements-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Endorsement Info\.csv\z"))]
    (read-and-add-primary file primary)))

(defpipe linkedin-recommendations-given-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Recommendations Given\.csv\z"))]
    (read-and-add-primary file primary)))

(defpipe linkedin-recommendations-received-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Recommendations Received\.csv\z"))]
    (read-and-add-primary file primary)))

(defpipe linkedin-skills-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Skills\.csv\z"))]
    (read-and-add-primary file primary)))

(defpipe linkedin-ad-targeting-pipeline
  [path-to-directory primary]
  (let [file (first (filter-csv path-to-directory
                                #"Ad Targeting\.csv\z"))]
    (read-and-add-primary file primary)))

