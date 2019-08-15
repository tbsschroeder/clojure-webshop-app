(ns metro.components.web.pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]))

(defn test? [service-map]
  (= :test (:env service-map)))

(defrecord Pedestal [service-map service]
  component/Lifecycle
  (start [this]
    (if service
      this
      (cond-> service-map
        true                      (http/create-server)  ;; always create http server
        (not (test? service-map)) (http/start)  ;; start http server if not in testing environment
        true                      ((partial assoc this :service)))))

  (stop [this]
    (when (and service (not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))

(defn new-pedestal []
  (map->Pedestal {}))
