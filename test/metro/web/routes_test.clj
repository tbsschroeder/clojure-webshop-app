(ns metro.web.routes-test
  (:require [io.pedestal.test :refer [response-for]]
            [clojure.test :refer [deftest is are testing]]
            [clojure.string :as string]
            [metro.components.server.routes :as routes]
            [metro.system-test :as test]))

(deftest greeting-test
  (testing "Greeting route should print typical hello world."
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status body]} (response-for service
                                                :get
                                                (routes/url-for :greet))]
        (is (= 200 status))
        (is (= "Hello, world!" body))))))

(deftest shop-test
  (testing "Main route should response the shop"
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status body]} (response-for service
                                                :get
                                                (routes/url-for :index))]
        (is (= 200 status))
        (is (string/includes? body "Amazing Web Shop Application"))
        (is (string/includes? body "Bacon"))
        (is (string/includes? body "+"))
        (is (string/includes? body "-"))
        (is (string/includes? body "Checkout"))))))

(deftest checkout-test
  (testing "Checkout route should response the checkout cat page"
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status body]} (response-for service
                                                :get
                                                (routes/url-for :checkout))]
        (is (= 200 status))
        (is (string/includes? body "Checkout"))
        (is (string/includes? body "cat"))))))

(deftest increment-test
  (testing "Post to increment will forward to main page"
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status]} (response-for service
                                           :post
                                           (routes/url-for :inc-article))]
        (is (= 302 status))))))

(deftest decrement-test
  (testing "Post to decrement will forward to main page"
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status]} (response-for service
                                           :post
                                           (routes/url-for :dec-article))]
        (is (= 302 status))))))

(deftest remove-test
  (testing "Should remove an article from the basket"
    (test/with-system [sut test/system]
      (let [service (test/service-fn sut)
            {:keys [status]} (response-for service
                                           :post
                                           (routes/url-for :rem-article))]
        (is (= 302 status))))))
