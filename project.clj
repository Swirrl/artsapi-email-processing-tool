(defproject artsapi-graft "0.1.0-SNAPSHOT"
  :description "A tool for converting datasets into RDF as part of the ArtsAPI project."
  :url "https://github.com/Swirrl/artsapi-email-processing-tool"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [grafter "0.3.0"]
                 [org.mnode.mstor/mstor "0.9.13"]
                 [digest "1.4.4"]
                 [cheshire "5.4.0"]]
  :aot [artsapi-graft.core]
  :main artsapi-graft.core)
