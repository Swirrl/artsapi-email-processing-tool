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
  (graph-fn [[first-name last-name email-address
              current-company current-position owner-email]]
            (graph person-graph-uri
                   [(resource-uri "people" email-address)
                    [rdf:a foaf:Person]
                    [foaf:account (resource-uri "linkedin-accounts" email-address)]
                    [foaf:givenName (s first-name)]
                    [foaf:familyName (s last-name)]
                    [vcard:hasEmail (s email-address)]
                    [foaf:mbox (s email-address)]
                    [arts:position (s current-position)]
                    [arts:connection (resource-uri "people" owner-email)]])

            (graph person-graph-uri
                   [(resource-uri "people" owner-email)
                    [rdf:a foaf:Person]
                    [foaf:account (resource-uri "linkedin-accounts" owner-email)]
                    [arts:connection (resource-uri "people" email-address)]])

            (graph linkedin-account-graph-uri
                   [(resource-uri "linkedin-accounts" email-address)
                    [rdf:a arts:LinkedInAccount]
                    [foaf:givenName (s first-name)]
                    [foaf:familyName (s last-name)]
                    [foaf:name (s (str first-name
                                       " "
                                       last-name))]
                    [vcard:hasEmail (s email-address)]])
            
            (graph linkedin-account-graph-uri
                   [(resource-uri "linkedin-accounts" owner-email)
                    [rdf:a arts:LinkedInAccount]
                    [vcard:hasEmail (s owner-email)]])))

(def endorsements-template
  (graph-fn [[endorsement-date skill-name endorser-first-name
              endorser-last-name owner-email]]
            (graph linkedin-endorsement-graph-uri
                   [(linkedin-hash "endorsements" endorsement-date
                                   endorser-first-name endorser-last-name)
                    [rdf:a arts:LinkedInEndorsement]
                    [arts:hasLinkedInSkill (resource-uri "linkedin-skill" skill-name)]
                    [arts:endorser (s (str endorser-first-name
                                           " "
                                           endorser-last-name))]
                    [arts:endorsee (resource-uri "people" owner-email)]
                    [arts:receivedAt endorsement-date]])

            (graph linkedin-skill-graph-uri
                   [(resource-uri "linkedin-skill" skill-name)
                    [rdf:a arts:LinkedInSkill]
                    [rdfs:label (s skill-name)]])))

(def recommendations-given-template
  (graph-fn [[recommendation-date recommendee-first-name
              recommendee-last-name owner-email]]
            (graph linkedin-recommendation-graph-uri
                   [(linkedin-hash "recommendations" recommendation-date
                                   recommendee-first-name recommendee-last-name)
                    [rdf:a arts:LinkedInRecommendation]
                    [arts:recommender (resource-uri "people" owner-email)]
                    [arts:recommendee (s (str recommendee-first-name
                                              " "
                                              recommendee-last-name))]
                    [arts:receivedAt recommendation-date]])))

(def recommendations-received-template
  (graph-fn [[recommendation-date recommender-first-name
              recommender-last-name owner-email]]
            (graph linkedin-recommendation-graph-uri
                   [(linkedin-hash "recommendations" recommendation-date
                                   recommender-first-name recommender-last-name)
                    [rdf:a arts:LinkedInRecommendation]
                    [arts:recommender (s (str recommender-first-name
                                              " "
                                              recommendee-last-name))]
                    [arts:recommendee (resource-uri "people" owner-email)]
                    [arts:receivedAt recommendation-date]])))

(def skills-template
  (graph-fn [[skill owner-email]]
            (graph linkedin-skill-graph-uri
                   [(resource-uri "linkedin-skill" skill)
                    [rdf:a arts:LinkedInSkill]
                    [rdfs:label (s skill)]])

            (graph person-graph-uri
                   [(resource-uri "people" owner-email)
                    [rdf:a foaf:Person]
                    [foaf:account (resource-uri "linkedin-accounts" owner-email)]])

            (graph linkedin-account-graph-uri
                   [(resource-uri "linkedin-accounts" owner-email)
                    [rdf:a arts:LinkedInAccount]
                    [vcard:hasEmail (s owner-email)]
                    [arts:hasLinkedInSkill (resource-uri "linkedin-skills" skill)]])))


;; we want to process this information into a table elsewhere to get
;; out the useful things we want to turn into triples
(def ad-targeting-template
  (graph-fn [[age-group country company-sizes companies followed-companies
              functions gender industries followed-industries partner-ads
              seniorities state zip-code schools graduation-year
              groups language degree-classes skills]]))

