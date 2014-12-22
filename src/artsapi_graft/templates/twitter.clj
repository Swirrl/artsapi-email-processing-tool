(ns artsapi-graft.templates.twitter
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]
            [artsapi-graft.twitter :refer :all]))

(defn tweet-template
  [{:keys [created_at entities user text]}]

  (let [name (user :name)
        screen-name (user :screen_name)]
    
    (concat
     (graph twitter-account-graph-uri
            [(resource-uri "twitter-accounts" screen-name)
             [rdf:a arts:TwitterAccount]
             [foaf:accountName (s screen-name)]
             [foaf:nick (s name)]])

     (graph tweet-graph-uri
            [(tweet-uri screen-name created_at text)
             [rdf:a arts:Tweet]
             [arts:tweetSender (resource-uri "twitter-accounts" screen-name)]
             [arts:content (s text)]]))))

(defn mentions-template
  [tweet {:keys [name screen_name id]}]

  (let [creator (get-screen-name tweet)
        created-at (get-created-at tweet)
        recipient-screen-name screen_name
        recipient-name name
        content (get-content tweet)]
    
    (concat
     (graph twitter-account-graph-uri
            [(resource-uri "twitter-accounts" recipient-screen-name)
             [rdf:a arts:TwitterAccount]
             [foaf:accountName (s recipient-screen-name)]
             [foaf:nick (s recipient-name)]])
     
     (graph tweet-graph-uri
            [(tweet-uri creator created-at content)
             [rdf:a arts:Tweet]
             [arts:tweetSender (resource-uri "twitter-accounts" creator)]
             [arts:mentions (resource-uri "twitter-accounts" recipient-screen-name)]]))))
