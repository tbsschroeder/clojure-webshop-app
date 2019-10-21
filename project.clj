(defproject metro "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensouArce.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]

                 ;; lifecycle
                 [com.stuartsierra/component "0.4.0"]

                 ;; http abstraction
                 [ring/ring-core "1.7.1"]

                 ;; html in clojure
                 [hiccup "1.0.5"]

                 ;; database
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.postgresql/postgresql "42.2.8"]
                 [org.xerial/sqlite-jdbc "3.28.0"]
                 [korma "0.4.3"]

                 ;; web server
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[io.pedestal/pedestal.service-tools "0.5.7"]]
                       :plugins      [[lein-kibit "0.1.6"]  ; static code analyzer
                                      [lein-ancient "0.6.15"] ; dependency checker
                                      [lein-cljfmt "0.6.0"] ; code formatter
                                      [jonase/eastwood "0.2.9"] ; lint tool
                                      ]}}
  :target-path "target/%s"
  :main ^{:skip-aot false} metro.system
  )
