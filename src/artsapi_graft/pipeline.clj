(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.store :refer :all]
            [artsapi-graft.templates.email :refer :all]))

(defn get-sender-email-ds
  [messages]
  (make-dataset messages [:from :from-personal :from-domain :sent-date :subject]))

(defn sender-pipeline
  [messages]
  (-> (get-sender-email-ds messages)
      (sender-email-template)))

(defn to-field-pipeline
  [messages]
  (mapcat (fn [msg]
            (mapcat to-email-template (msg :to)))
       messages))

(defn cc-field-pipeline
  [messages]
  (mapcat (fn [msg]
            (mapcat cc-email-template (msg :cc)))
       messages))

(defn email-pipeline
  [store]
  (let [messages (->> [:default "inbox" "sent"] (get-all-messages store))]
    (lazy-cat (sender-pipeline messages)
         (to-field-pipeline messages)
         (cc-field-pipeline messages))))

