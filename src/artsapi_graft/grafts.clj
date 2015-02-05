(ns artsapi-graft.grafts
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.templates.linkedin :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-mentions
                                                     get-tweets-from-archive]]
            [artsapi-graft.pipeline :refer :all]))

;; not strictly grafts

(defn email-sender-pipeline
  [messages]
  (-> (get-sender-email-ds messages)
      (sender-email-template)))

(defn to-field->quads
  [messages]
  (mapcat (fn [msg]
            (mapcat to-email-template (msg :to)))
          messages))

(defn cc-field->quads
  [messages]
  (mapcat (fn [msg]
            (mapcat cc-email-template (msg :cc)))
          messages))

(defn tweet-sender->quads
  [tweets]
  (mapcat #(tweet-template %) tweets))

(defn mentions->quads
  [tweets]
  (mapcat (fn [tweet]
            (when-not (empty? (tweet/get-mentions tweet))
              (mapcat (fn [mention]
                        (mentions-template tweet mention))
                      (tweet/get-mentions tweet))))
          tweets))

(defn linkedin-owner->quads
  [emails primary]
  (-> (add-column emails :primary primary)
      owner-template))

;; now these are the grafts we are looking for

(defgraft linkedin-connections-graft
  linkedin-connections-pipeline
  connections-template)

(defgraft linkedin-endorsements-graft
  linkedin-endorsements-pipeline
  endorsements-template)

(defgraft linkedin-recommendations-given-graft
  linkedin-recommendations-given-pipeline
  recommendations-given-template)

(defgraft linkedin-recommendations-received-graft
  linkedin-recommendations-received-pipeline
  recommendations-received-template)

(defgraft linkedin-skills-graft
  linkedin-skills-pipeline
  skills-template)

(defgraft linkedin-ad-targeting-graft
  linkedin-ad-targeting-pipeline
  ad-targeting-template)
