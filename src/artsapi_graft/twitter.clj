(ns artsapi-graft.twitter
  (:require [artsapi-graft.json :refer :all]))

(defn get-tweets-map-from
  "Returns a lazy sequence of maps. Each map represents a tweet"
  [file-path]
  (-> (read-file-from-line-two file-path)
      (json->keyword-hash)))

(defn get-created-at
  [tweet]
  (tweet :created_at))

(defn get-mentions
  [tweet]
  ((tweet :entities) :user_mentions))

(defn get-user
  [tweet]
  (tweet :user))

;; a couple of utility shorthands

(defn get-user-name
  [tweet]
  ((get-user tweet) :name))

(defn get-screen-name
  [tweet]
  ((get-user tweet) :screen_name))

(defn get-id
  [tweet]
  ((get-user tweet) :id))

;; we may not use these yet but I see them becoming useful

(defn get-content
  [tweet]
  (tweet :text))

(defn get-hashtags
  [tweet]
  ((tweet :entities) :hashtags))

(defn get-urls
  [tweet]
  ((tweet :entities) :urls))

(defn open-twitter-archive
  "A twitter archive is full of js files.
   We want to get all the js files so we can snip off line one
   and get at the sweet, sweet JSON inside."
  [path-to-directory]
  (->> (open-file-directory path-to-directory)
       (filter #(re-find #"\.js\z" %))))

(defn archive->lazy-seq
  [path-to-directory]
  (mapcat get-tweets-map-from (open-twitter-archive path-to-directory)))

(defn get-tweets-from-archive
  [path-to-directory]
  (archive->lazy-seq path-to-directory))
