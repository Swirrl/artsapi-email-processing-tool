(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.store :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-mentions
                                                     get-tweets-from-archive]]))

(defn init-store [path]
  (store path))

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

(defpipe email-pipeline
  [path]
  (let [store (init-store path)] 
    (let [messages (->> [:default "inbox" "sent"] (get-all-messages store))]
      (lazy-cat (sender-pipeline messages)
                (to-field-pipeline messages)
                (cc-field-pipeline messages)))))

(defn tweet-sender-pipeline
  [tweets]
  (mapcat #(tweet-template %) tweets))

(defn mentions-pipeline
  [tweets]
  (mapcat (fn [tweet]
            (when-not (empty? (tweet/get-mentions tweet))
              (mapcat (fn [mention]
                        (mentions-template tweet mention))
                      (tweet/get-mentions tweet))))
          tweets))

(defpipe twitter-pipeline
  [tweet-directory]
  (let [tweets (get-tweets-from-archive tweet-directory)]
    (lazy-cat (tweet-sender-pipeline tweets)
              (mentions-pipeline tweets))))

(defpipe linkedin-connections-pipeline
  [path-to-directory])

(defpipe linkedin-endorsements-pipeline
  [path-to-directory])

(defpipe linkedin-recommendations-pipeline
  [path-to-directory])

(defpipe linkedin-skills-pipeline
  [path-to-directory])

(defpipe linkedin-ad-targeting-pipeline
  [path-to-directory])

(defn linkedin-pipeline
  [])
