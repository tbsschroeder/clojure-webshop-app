(ns metro.components.db.postgres
  (:require [com.stuartsierra.component :as component]
            [korma.db :as kdb]
            [metro.components.db.articles :as articles]))

(def status (atom "down"))

(defrecord Postgres [db-config database]
  component/Lifecycle
  (start [this]
    (let [db (kdb/create-db (kdb/postgres db-config))]
      (kdb/default-connection db)
      (articles/create-table! false)
      (assoc this :database db))
    (reset! status "up"))
  (stop [this]
    (kdb/default-connection nil)
    (assoc this :database nil)
    (reset! status "down")))

(defn new-database []
  (map->Postgres {}))
