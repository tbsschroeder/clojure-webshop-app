(ns metro.db.articles-test
  (:require [io.pedestal.test :refer [response-for]]
            [clojure.test :refer [deftest is are testing]]
            [metro.components.db.articles :as articles]
            [metro.system-test :as test]))

(defn- get-first-count []
  (:count (first (articles/query-all))))

(deftest db-tests
  (test/with-system [sut test/system]
    (testing "Test db has data"
      (is (< 0 (count (articles/query-all)))))))

(deftest query-all-tests
  (test/with-system [sut test/system]
    (testing "Add an article in the db"
      (let [sum-total-articles (count (articles/query-all))]
        (articles/add! "foo" "description" "category" "img-path" 1)
        (is (= (inc sum-total-articles)
               (count (articles/query-all))))))))

(deftest inc-tests
  (test/with-system [sut test/system]
    (testing "Increase an articles count in the db"
      (let [count-article (get-first-count)]
        (articles/inc! 1)
        (is (= (inc count-article) (get-first-count)))))))

(deftest dec-tests
  (test/with-system [sut test/system]
    (testing "Decrease an articles count in the db"
      (let [count-article (get-first-count)]
        (articles/dec! 1)
        (is (= (dec count-article)
               (get-first-count)))))))

(deftest rem-tests
  (test/with-system [sut test/system]
    (testing "Set articles count to 0"
      (let [count-article (get-first-count)]
        (articles/inc! 1)
        (is (< count-article
               (get-first-count)))
        (articles/rem! 1)
        (is (= count-article
               (get-first-count)))))))

(deftest del-tests
  (test/with-system [sut test/system]
    (testing "Remove an article in the db"
      (let [sum-total-articles (count (articles/query-all))]
        (articles/delete! 1)
        (is (= (dec sum-total-articles)
               (count (articles/query-all))))))))

(deftest add-with-id-tests
  (test/with-system [sut test/system]
    (testing "Add an article in the db incl id"
      (let [sum-total-articles (count (articles/query-all))]
        (articles/add-with-id! 15
                               "Bacon2"
                               "Bacon2 is a type of salt-cured pork. Bacon is prepared from several different cuts of meat, typically from the pork belly or from back cuts, which have less fat than the belly. And it's delicious!"
                               "Meat2"
                               "img/products/bacon.jpg"
                               0)
        (is (= (inc sum-total-articles)
               (count (articles/query-all))))
        (is (= 15
               (:id (last (articles/query-all)))))))))

; not working right now
;(deftest get-count-tests
;  (test/with-system
;    [sut test/system]
;    (testing "Get count of basket"
;      (let [article_count (articles/get-count! 2)]
;        (is (= 0
;               article_count))))
;    (testing "Increase element and get count"
;      (articles/inc! 1)
;      (let [article_count_new (articles/get-count! 2)]
;        (is (= 1
;               article_count_new))))
;    (testing "Decrease element and get count"
;      (articles/dec! 1)
;      (let [article_count (articles/get-count! 2)]
;        (is (= 0
;               article_count))))))

(deftest query-for-article-tests
  (test/with-system [sut test/system]
    (testing "Get bacon"
      (let [article (articles/query-article 1)]
        (is (= 1 (:id article)))
        (is (= "Bacon" (:title article)))
        (is (= "Meat" (:category article)))
        (is (= 0 (:count article)))))))

(deftest query-all-with-count-tests
  (test/with-system [sut test/system]
    (testing "Get all article with count > 0"
      (let [articles (articles/query-all-with-count)
            articles-w-count (count articles)]
        (is (= 0 articles-w-count))
        (articles/inc! 1)
        (is (= (inc articles-w-count) (count (articles/query-all-with-count))))))))

(deftest has-articles-with-data-tests
  (test/with-system [sut test/system]
    (testing "False if there are no articles in the basket"
      (let [val (articles/has-articles-with-data?)]
        (is (not val))))
    (testing "True if there are articles in the basket"
      (articles/inc! 1)
      (let [val (articles/has-articles-with-data?)]
        (is val)))))
