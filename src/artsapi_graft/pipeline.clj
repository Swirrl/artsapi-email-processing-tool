(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
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

(defn get-non-seq-email-fields
  [messages]
  (make-dataset messages [:from :from-personal :from-domain :sent-date :subject]))

;; after that we want to use all the info about ccs and tos to create
;; more people and attach those recipients to the email using the
;; right predicates

;; we make a ds out of the former and proceed directly to
;; quad-ification with the latter

(defn email-pipeline
  [store]
  (let [messages (->> [:default "inbox" "sent"] (get-all-messages store))]
    (get-non-seq-email-fields messages)))

