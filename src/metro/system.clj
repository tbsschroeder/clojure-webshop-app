(ns metro.system
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [metro.components.server.pedestal :as pedestal]
            [metro.components.server.routes :as routes]
            [metro.components.db.postgres :as postgres]))

(defn- build-service-map [env]
  ;; Pedestals web server configuration (http://pedestal.io/reference/service-map).
  {:env env
   ;; Routes can be a function that resolve routes
   ::http/routes routes/routes
   ;; Define server type
   ::http/type :jetty
   ;; Is it docker or local?
   ::http/host (or (System/getenv "WEB_HOST") "localhost")
   ;; Port to listen on
   ::http/port 8080
   ;; Path of public resources
   ::http/resource-path "/public"
   ;; All origins are allowed in dev mode
   ::http/allowed-origins {:creds true :allowed-origins (constantly true)}
   ;; Content Security Policy (CSP) is mostly turned off in dev mode
   ::http/secure-headers {:content-security-policy-settings {:object-src "'none'"}}
   })

(defn system [env]
  ;; Passing components to the main system
  (component/system-map
   :service-map (build-service-map env)
   :db-config {:db       "clojure"
               :user     "clojure"
               :password "clojure"
               :host      (or (System/getenv "DB_HOST") "localhost")}
   :db (component/using (postgres/new-database) [:db-config])
   :web (component/using (pedestal/new-pedestal) [:db :service-map])))

(defn -main [& _]
  (component/start (system {})))
