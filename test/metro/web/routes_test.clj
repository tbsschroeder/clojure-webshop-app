(ns metro.web.routes-test
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [clojure.test :refer [deftest is are testing]]
            [metro.components.web.routes :as routes]
            [metro.system-test :as test]))

(defn- service-fn
  [system]
  (get-in system [:web :service ::http/service-fn]))

(deftest greeting-test
  (testing "Greeting route should print typical hello world."
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status body]} (response-for service
                                                :get
                                                (routes/url-for :greet))]
        (is (= 200 status))
        (is (= "Hello, world!" body))))))