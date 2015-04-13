(ns artsapi-graft.grafts
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.templates.linkedin :refer :all]
            [artsapi-graft.templates.email :refer :all]
            [artsapi-graft.templates.twitter :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-mentions
                                                     get-tweets-from-archive]]
            [artsapi-graft.pipeline :refer :all]
            [artsapi-graft.keywords :refer [included-keywords]]
            [artsapi-graft.prefixers :as prefixers :refer [resource-uri]]))

;; not strictly grafts

(defn get-mbox-owner-uri
  [messages]
  (let [first-message (take 1 messages)
        from (:from first-message)]
    (prefixers/resource-uri "people" from)))

(defn email-sender-pipeline
  [messages]
  (-> (get-sender-email-ds messages)
      (sender-email-template)))

(defn owner-metadata->quads
  [owner-uri inbox-messages sent-messages]
  (lazy-cat
   (incoming-emails-template owner-uri inbox-messages)
   (sent-emails-template owner-uri sent-messages)))

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

(defn keywords->quads
  [messages]
  (mapcat (fn [msg]
            (let [included-words (included-keywords (msg :content))]
              (mapcat #(keyword-email-template % msg) included-words)))
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

;; now these are the grafts we are looking for

(defgraft linkedin-owner-graft
  linkedin-owner-pipeline
  owner-template)

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
