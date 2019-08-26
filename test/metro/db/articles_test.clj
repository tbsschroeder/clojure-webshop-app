(ns metro.db.articles-test
  (:require [io.pedestal.test :refer [response-for]]
            [clojure.test :refer [deftest is are testing]]
            [metro.components.db.articles :as articles]
            [metro.system-test :as test]))

(defn- get-first-count []
  (:count (first (articles/query-all))))

(deftest article-tests
  (test/with-system
    [sut test/system]

    (testing "Test db has data"
      (is (< 0 (count (articles/query-all)))))

    (testing "Add an article in the db"
      (let [sum-total-articles (count (articles/query-all))]
        (articles/add! "foo" "description" "category" "img-path" 1)
        (is (= (inc sum-total-articles)
               (count (articles/query-all))))))

    (testing "Increase an articles count in the db"
      (let [count-article (get-first-count)]
        (articles/inc! 1)
        (is (= (inc count-article) (get-first-count)))))

    (testing "Decrease an articles count in the db"
      (let [count-article (get-first-count)]
        (articles/dec! 1)
        (is (= (dec count-article)
               (get-first-count)))))

    (testing "Set articles count to 0"
      (let [count-article (get-first-count)]
        (articles/inc! 1)
        (is (< count-article
               (get-first-count)))
        (articles/rem! 1)
        (is (= count-article
               (get-first-count)))))

    (testing "Remove an article in the db"
      (let [sum-total-articles (count (articles/query-all))]
        (articles/delete! 1)
        (is (= (dec sum-total-articles)
               (count (articles/query-all))))))))