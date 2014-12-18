(ns artsapi-graft.templates.email
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defn generate-mailto-uri
  "Pass this a string containing an email address and have a mailto string returned."
  [address]
  (str "mailto:" address))

(def sender-email-template
  (graph-fn [[from from-personal from-domain sent-date subject]]

            (graph email-graph-uri
                   [(email-uri from sent-date subject)
                    [rdfs:label subject]
                    [rdf:a arts:Email]
                    [arts:emailSender (resource-uri "people" from)]
                    [arts:emailSubject subject]
                    [arts:hasDomain (resource-uri "domains" from-domain)]
                    [arts:sentAt sent-date]])

            (graph person-graph-uri
                   [(resource-uri "people" from)
                    [rdfs:label from-personal]
                    [rdf:a foaf:Person]
                    [foaf:name from-personal]
                    [vcard:hasEmail from]
                    [foaf:mbox from]
                    [org:memberOf (resource-uri "organisations" from-domain)]
                    [foaf:made (email-uri from sent-date subject)]])
            
            (graph organisation-graph-uri
                   [(resource-uri "organisations" from-domain)
                    [rdfs:label from-domain]
                    [rdf:a org:Organization]
                    [arts:ownsDomain (resource-uri "domains" from-domain)]
                    [org:hasMember (resource-uri "people" from)]])

            (graph domain-graph-uri
                   [(resource-uri "domains" from-domain)
                    [rdfs:label from-domain]
                    [rdf:a arts:Domain]
                    [vcard:hasUrl (str "http://" from-domain)]])))

(defn to-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:emailRecipient (resource-uri "people" email)]])

   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label personal]
           [rdf:a foaf:Person]
           [foaf:name personal]
           [vcard:hasEmail email]
           [foaf:mbox email]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label domain]
           [rdf:a org:Organization]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])

   (graph domain-graph-uri
          [(resource-uri "domains" domain)
           [rdfs:label domain]
           [rdf:a arts:Domain]
           [vcard:hasUrl (str "http://" domain)]])))

(defn cc-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:ccRecipient (resource-uri "people" email)]])

   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label personal]
           [rdf:a foaf:Person]
           [foaf:name personal]
           [vcard:hasEmail email]
           [foaf:mbox email]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label domain]
           [rdf:a org:Organization]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])

   (graph domain-graph-uri
          [(resource-uri "domains" domain)
           [rdfs:label domain]
           [rdf:a arts:Domain]
           [vcard:hasUrl (str "http://" domain)]])))

