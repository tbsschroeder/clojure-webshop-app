(ns metro.components.web.health
  (:require [clojure.data.json :as json]
            [metro.components.server.pedestal :as server]
            [metro.components.db.postgres :as postgres]))

(defn health [request]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:server @server/status
                             :database @postgres/status})})