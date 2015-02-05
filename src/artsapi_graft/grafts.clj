(ns artsapi-graft.grafts
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.templates.linkedin :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-mentions
                                                     get-tweets-from-archive]]
            [artsapi-graft.pipeline :refer :all]))

;; not strictly grafts

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
  [path-to-directory primary]
  (linkedin-connections-pipeline path-to-directory primary))

(defgraft linkedin-endorsements-graft
  [path-to-directory primary]
  (linkedin-endorsements-pipeline path-to-directory primary))

(defgraft linkedin-recommendations-given-graft
  [path-to-directory primary]
  (linkedin-recommendations-given-pipeline path-to-directory primary))

(defgraft linkedin-recommendations-received-graft
  [path-to-directory primary]
  (linkedin-recommendations-received-pipeline path-to-directory primary))

(defgraft linkedin-skills-graft
  [path-to-directory primary]
  (linkedin-skills-pipeline path-to-directory primary))

(defgraft linkedin-ad-targeting-graft
  [path-to-directory primary]
  (linkedin-ad-targeting-pipeline path-to-directory primary))
