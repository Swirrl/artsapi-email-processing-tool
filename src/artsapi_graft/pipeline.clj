(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [artsapi-graft.store :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defn make-email-datasets
  "Get all messages from the supplied store. Tries the default folder, inbox and sent folders.
   The store should correspond to a single instance of an mbox, created using artsapi-graft.store/store"
  [store]
  (-> (->> [:default "inbox" "sent"] (get-all-messages store)
           (make-dataset [:from :from-personal :from-domain 
                          :subject (:cc :personal) (:cc :email) (:cc :domain)]))))

;; we want to separate out the from, from-personal, from-domain,
;; sent-date and subject fields into an email and create an associated
;; person

(defn get-sender-email-ds
  [messages]
  (make-dataset messages [:from :from-personal :from-domain :sent-date :subject]))

(def sender-email-template
  (graph-fn [[from from-personal from-domain sent-date subject]]

            (graph email-graph-uri
                   [(email-uri from sent-date subject)
                    [rdfs:label subject]
                    [arts:emailSender (resource-uri "people" from)]
                    [arts:emailSubject subject]
                    [arts:hasDomain (resource-uri "domains" from-domain)]
                    [arts:sentAt sent-date]])

            (graph person-graph-uri
                   [(resource-uri "people" from)
                    [rdfs:label from-personal]
                    [foaf:name from-personal]
                    [vcard:hasEmail from]
                    [foaf:mbox from]
                    [org:memberOf (resource-uri "organisations" from-domain)]
                    [foaf:made (email-uri from sent-date subject)]])
            
            (graph organisation-graph-uri
                   [(resource-uri "organisations" from-domain)
                    [rdfs:label from-domain]
                    [arts:ownsDomain (resource-uri "domains" from-domain)]
                    [org:hasMember (resource-uri "people" from)]])))

;; after that we want to use all the info about ccs and tos to create
;; more people and attach those recipients to the email using the
;; right predicates

(defn to-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:emailRecipient (resource-uri "people" email)]])

   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label personal]
           [foaf:name personal]
           [vcard:hasEmail email]
           [foaf:mbox email]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label domain]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])))

(defn cc-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:ccRecipient (resource-uri "people" email)]])

   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label personal]
           [foaf:name personal]
           [vcard:hasEmail email]
           [foaf:mbox email]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label domain]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])))

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

