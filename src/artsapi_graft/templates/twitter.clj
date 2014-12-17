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

  (concat
   (graph)
   ))

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
             [foaf:accountName recipient-screen-name]
             [foaf:nick recipient-name]])
     
     (graph tweet-graph-uri
            [(tweet-uri creator created-at content)
             [arts:tweetSender (resource-uri "twitter-accounts" creator)]
             [arts:content content]
             [arts:mentions recipient-screen-name]]))))
