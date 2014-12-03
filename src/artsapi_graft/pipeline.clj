(ns artsapi-graft.pipeline
  (:require [grafter.tabular :refer :all]
            [artsapi-graft.store :refer :all]
            [artsapi-graft.ontologies :refer :all]
            [artsapi-graft.prefixers :refer :all]))

(defn make-email-ds
  "Get all messages from the supplied store. Tries the default folder, inbox and sent folders.
   The store should correspond to a single instance of an mbox, created using artsapi-graft.store/store"
  [store]
  (-> (->> [:default "inbox" "sent"] (get-all-messages store)
           (make-dataset [:from :from-personal :from-domain
                          (:to :personal) (:to :email) (:to :domain)
                          :subject (:cc :personal) (:cc :email) (:cc :domain)]))))

(defn email-pipeline
  [dataset]
  (-> dataset
      ))

