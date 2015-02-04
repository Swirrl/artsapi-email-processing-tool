(ns artsapi-graft.templates.linkedin
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.org :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(def connections-template
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

(def endorsements-template
  (graph-fn [[endorsement-date skill-name endorser-first-name endorser-last-name]]
            (graph )))

(def recommendations-given-template
  (graph-fn [[recommendation-date recommendee-first-name recommendee-last-name]]))

(def recommendations-received-template
  (graph-fn [[recommendation-date recommender-first-name recommender-last-name]]))

(def skills-template
  (graph-fn [[skill]]))

(def ad-targeting-template
  (graph-fn [[age-group country company-sizes companies followed-companies functions gender industries followed-industries partner-ads seniorities state zip code schools graduation-year groups language degree-classes skills]]))

;; here be defgrafts

(defgraft linkedin-connections-graft)

(defgraft linkedin-endorsements-graft)

(defgraft linkedin-recommendations-given-graft)

(defgraft linkedin-recommendations-received-graft)

(defgraft linkedin-skills-graft)

(defgraft linkedin-ad-targeting-graft)
