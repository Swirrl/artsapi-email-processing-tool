(ns artsapi-graft.templates.linkedin
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defgraft connections-template
  (graph-fn [[first-name last-name email-address current-company current-position]]
            (graph person-graph-uri
                   [(resource-uri "people" email-address)
                    [rdf:a foaf:Person]
                    [foaf:givenName (s first-name)]
                    [foaf:familyName (s last-name)]
                    [vcard:hasEmail (s email-address)]
                    [foaf:mbox (s email-address)]
                    [arts:position (s current-position)]
                    [arts:connection ]])))

(defgraft endorsements-template
  (graph-fn [[endorsement-date skill-name endorser-first-name endorser-last-name]]
            (graph )))

(defgraft recommendations-given-template
  (graph-fn [[recommendation-date recommendee-first-name recommendee-last-name]]))

(defgraft recommendations-received-template
  (graph-fn [[recommendation-date recommender-first-name recommender-last-name]]))

(defgraft skills-template
  (graph-fn [[skill]]))

(defgraft ad-targeting-template
  (graph-fn [[age-group country company-sizes companies followed-companies functions gender industries followed-industries partner-ads seniorities state zip code schools graduation-year groups language degree-classes skills]]))
