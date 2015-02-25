(defproject artsapi-graft "2.1.0"
  :description "A tool for converting datasets into RDF as part of the ArtsAPI project."
  :url "https://github.com/Swirrl/artsapi-email-processing-tool"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [grafter "0.3.1"]
                 [org.mnode.mstor/mstor "0.9.13"]
                 [clojure-mail "0.1.6"]
                 [digest "1.4.4"]
                 [cheshire "5.4.0"]]
  :jvm-opts ["-Xmx7g"]
  :main artsapi-graft.core)
