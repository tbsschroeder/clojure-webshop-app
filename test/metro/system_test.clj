(ns metro.system-test
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [com.stuartsierra.component :as component]
            [clojure.test :refer [deftest is are testing]]
            [metro.system :as system]
            [metro.components.web.routes :as routes]
            [korma.db :as kdb]
            [metro.components.db.articles :as articles]))

(defrecord TestDB [db-config database]
  component/Lifecycle
  (start [this]
    (let [db (kdb/create-db (kdb/sqlite3 {:db "testing.sqlite3"}))]
      (kdb/default-connection db)
      (articles/drop-table!)
      (articles/create-table!)
      (assoc this :database db)))
  (stop [this]
    (articles/drop-table!)
    (kdb/default-connection nil)
    (assoc this :database nil)))

(defn- new-test-db []
  (map->TestDB {}))

;; -----------------------------------------------------------------------------

(def test-system
  (assoc (system/system :test)
         :db (new-test-db)))  ;; Inject TestDB for testing

(defmacro with-system
  [[bound-var test-system] & body]
  `(let [~bound-var (component/start ~test-system)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn- service-fn
  [system]
  (get-in system [:web :service ::http/service-fn]))

;; -----------------------------------------------------------------------------

(deftest greeting-test
  (testing "Greeting route should print typical hello world."
    (with-system [sut test-system]
      (let [service (service-fn sut)
            {:keys [status body]} (response-for service
                                                :get
                                                (routes/url-for :greet))]
        (is (= 200 status))
        (is (= "Hello, world!" body))))))

(deftest add-todo-test
  (testing "Add an article fo the db"
    (with-system [sut test-system]
      (let [sum-total-articles (count (articles/query-all))]
        (articles/add! "foo" "description" "category" "img-path" 0)
        (is (= (inc sum-total-articles) (count (articles/query-all))))))))

(comment
  (def mytestsystem (component/start test-system))
  mytestsystem)
