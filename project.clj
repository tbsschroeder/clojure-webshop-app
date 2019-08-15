(defproject metro "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stuartsierra/component "0.3.2"]

                 [hiccup "1.0.5"]

                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.4"]
                 [org.xerial/sqlite-jdbc "3.21.0"]
                 [korma "0.4.3"]

                 [io.pedestal/pedestal.service "0.5.4"]
                 [io.pedestal/pedestal.jetty "0.5.4"]

                 [ch.qos.logback/logback-classic "1.1.8" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "metro.server/run-dev"]}
  ;;                  :dependencies [[io.pedestal/pedestal.service-tools "0.5.4"]]}
  ;;            :uberjar {:aot [metro.server]}}
  :profiles {:uberjar {:aot :all}
             :dev     {:plugins      [[lein-kibit "0.1.6"]  ; static code analyzer
                                      [lein-ancient "0.6.15"] ; dependency checker
                                      [lein-cljfmt "0.6.0"] ; code formatter
                                      [jonase/eastwood "0.2.9"] ; lint tool
                                      ]}}
  :target-path "target/%s"
  :main ^{:skip-aot true} metro.system
  )
