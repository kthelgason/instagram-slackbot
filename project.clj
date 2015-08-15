(defproject clj-slackbot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.2.0"]
                 [clojail "1.0.6"]
                 [clj-http "1.0.1"]
                 [cheshire "5.3.1"]
                 [environ "1.0.0"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler instagram-slackbot.core.handler/app}
  :uberjar-name "instagram-slackbot.jar"
  :main instagram-slackbot.core.handler
  :profiles
  {:dev {:repl-options {:init-ns instagram-slackbot.core.handler}
         :dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}
   :uberjar {:aot :all}})
