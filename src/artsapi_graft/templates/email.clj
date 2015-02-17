(ns artsapi-graft.templates.email
  (:require [grafter.tabular :refer :all]
            [grafter.rdf.templater :refer :all]
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

(defn catch-nil->s
  "The s function, with the ability to represent nil as a string when used with arity of 1."
  [str]
  (if (nil? str)
    "nil"
    (s str)))

(def sender-email-template
  (graph-fn [[from from-personal from-domain sent-date subject]]

            (graph email-graph-uri
                   [(email-uri from sent-date subject)
                    ;;[rdfs:label (s subject)]
                    [rdf:a arts:Email]
                    [arts:emailSender (resource-uri "people" from)]
                    ;;[arts:emailSubject (s subject)]
                    [arts:hasDomain (resource-uri "domains" from-domain)]
                    [arts:sentAt (s sent-date)]])

            (graph person-graph-uri
                   [(resource-uri "people" from)
                    [rdfs:label (catch-nil->s from-personal)]
                    [rdf:a foaf:Person]
                    [foaf:name (catch-nil->s from-personal)]
                    [vcard:hasEmail (s from)]
                    [foaf:mbox (s from)]
                    [org:memberOf (resource-uri "organisations" from-domain)]
                    [foaf:made (email-uri from sent-date subject)]])
            
            (graph organisation-graph-uri
                   [(resource-uri "organisations" from-domain)
                    [rdfs:label (s from-domain)]
                    [rdf:a org:Organization]
                    [arts:ownsDomain (resource-uri "domains" from-domain)]
                    [org:hasMember (resource-uri "people" from)]])

            (graph domain-graph-uri
                   [(resource-uri "domains" from-domain)
                    [rdfs:label (s from-domain)]
                    [rdf:a arts:Domain]
                    [vcard:hasUrl (s (str "http://" from-domain))]])))

(defn to-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:emailRecipient (resource-uri "people" email)]])

   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label (catch-nil->s personal)]
           [rdf:a foaf:Person]
           [foaf:name (catch-nil->s personal)]
           [vcard:hasEmail (s email)]
           [foaf:mbox (s email)]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label (s domain)]
           [rdf:a org:Organization]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])

   (graph domain-graph-uri
          [(resource-uri "domains" domain)
           [rdfs:label (s domain)]
           [rdf:a arts:Domain]
           [vcard:hasUrl (s (str "http://" domain))]])))

(defn cc-email-template
  [{:keys [email-uri personal email domain]}]

  (concat 
   (graph email-graph-uri
          [email-uri
           [arts:ccRecipient (resource-uri "people" email)]])
   
   (graph person-graph-uri
          [(resource-uri "people" email)
           [rdfs:label (catch-nil->s personal)]
           [rdf:a foaf:Person]
           [foaf:name (catch-nil->s personal)]
           [vcard:hasEmail (s email)]
           [foaf:mbox (s email)]
           [org:memberOf (resource-uri "organisations" domain)]])

   (graph organisation-graph-uri
          [(resource-uri "organisations" domain)
           [rdfs:label (s domain)]
           [rdf:a org:Organization]
           [arts:ownsDomain (resource-uri "domains" domain)]
           [org:hasMember (resource-uri "people" email)]])

   (graph domain-graph-uri
          [(resource-uri "domains" domain)
           [rdfs:label (s domain)]
           [rdf:a arts:Domain]
           [vcard:hasUrl (s (str "http://" domain))]])))

(defn keywords-template 
  [keyword {:keys from sent-date subject}]

  (graph email-graph-uri
         [(email-uri from sent-date subject)
          [arts:containsKeyword (s keyword)]]))

