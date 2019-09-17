(ns metro.system
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [metro.components.web.pedestal :as pedestal]
            [metro.components.web.routes :as routes]
            [metro.components.db.postgres :as postgres]))

(defn- build-service-map [env]
  {:env env
   ::http/routes routes/routes
   ::http/type :jetty
   ::http/port 8080
   ::http/resource-path "/public"
   ::http/join? false})

(defn system [env]
  (component/system-map
   :service-map (build-service-map env)

   :db-config {:db       "clojure"
               :user     "clojure"
               :password "clojure"}

   :db (component/using
        (postgres/new-database)
        [:db-config])

   :web (component/using
         (pedestal/new-pedestal)
         [:db :service-map])))

(defn -main [& args]
  (component/start (system {})))

(comment
  (def mysystem (component/start (system {})))
  (component/stop mysystem)
  mysystem)
