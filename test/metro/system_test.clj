(ns metro.system-test
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [com.stuartsierra.component :as component]
            [clojure.test :refer [deftest is are testing]]
            [korma.db :as kdb]
            [metro.system :as system]
            [metro.components.db.articles :as articles]))

(defrecord TestDB [db-config database]
  component/Lifecycle
  (start [this]
    (let [db (kdb/create-db (kdb/sqlite3 {:db "testing.sqlite3"}))]
      (kdb/default-connection db)
      (articles/drop-table!)
      (articles/create-table! true)
      (assoc this :database db)))
  (stop [this]
    ;(articles/drop-table!)
    (kdb/default-connection nil)
    (assoc this :database nil)))

(def system
  (assoc (system/system :test)
         :db (map->TestDB {})))  ;; Inject TestDB for testing

(defmacro with-system
  [[bound-var test-system] & body]
  `(let [~bound-var (component/start ~test-system)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn service-fn
  [system]
  (get-in system [:web :service ::http/service-fn]))

(comment
  (def my-test-system (component/start system))
  my-test-system)
