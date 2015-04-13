(ns artsapi-graft.quad-converters
  (:require [artsapi-graft.grafts :refer :all]
            [artsapi-graft.store :refer :all]
            [artsapi-graft.linkedin :refer :all]
            [artsapi-graft.twitter :as tweet :refer [get-tweets-from-archive]]))

(defn init-store [path]
  (store path))

(defn mbox-folder->quads
  [messages]
  (lazy-cat (email-sender-pipeline messages)
            (to-field->quads messages)
            (cc-field->quads messages)
            (keywords->quads messages)))

(defn email->quads
  [path]
  (let [store (init-store path)]
    (let [inbox-messages (->> ["inbox"] (get-all-messages store))
          sent-messages (->> ["sent"] (get-all-messages store))
          mbox-owner (get-mbox-owner-uri sent-messages)]
      (lazy-cat
       (owner-metadata->quads mbox-owner
                              inbox-messages
                              sent-messages)
       (mbox-folder->quads inbox-messages)
       (mbox-folder->quads sent-messages)))))

(defn twitter->quads
  [tweet-directory]
  (let [tweets (tweet/get-tweets-from-archive tweet-directory)]
    (lazy-cat (tweet-sender->quads tweets)
              (mentions->quads tweets))))

(defn linkedin->quads
  [path-to-directory]
  (let [emails (get-linkedin-email-ds path-to-directory)
        primary (get-linkedin-primary-email path-to-directory)] 
    (lazy-cat (linkedin-owner-graft emails primary)
              (linkedin-connections-graft path-to-directory primary)
              (linkedin-endorsements-graft path-to-directory primary)
              (linkedin-recommendations-given-graft path-to-directory primary)
              (linkedin-recommendations-received-graft path-to-directory primary)
              (linkedin-skills-graft path-to-directory primary)
              (linkedin-ad-targeting-graft path-to-directory primary))))
