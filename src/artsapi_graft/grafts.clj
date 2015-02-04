(ns artsapi-graft.grafts
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.templates.linkedin :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-mentions
                                                     get-tweets-from-archive]]
            [artsapi-graft.pipeline :refer :all]))

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

(defgraft linkedin-connections-graft)

(defgraft linkedin-endorsements-graft)

(defgraft linkedin-recommendations-given-graft)

(defgraft linkedin-recommendations-received-graft)

(defgraft linkedin-skills-graft)

(defgraft linkedin-ad-targeting-graft)
