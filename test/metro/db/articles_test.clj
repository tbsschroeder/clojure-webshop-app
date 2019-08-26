(ns metro.db.articles-test
  (:require [io.pedestal.test :refer [response-for]]
            [clojure.test :refer [deftest is are testing]]
            [metro.components.db.articles :as articles]
            [metro.system-test :as test]))

(defn- get-first []
  (first (articles/query-all)))

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

    ;(testing "Increase an articles count in the db"
    ;  (let [count-article (:count (get-first))]
    ;    (articles/inc! 0)
    ;    (is (= (inc count-article) (:count (get-first))))
    ;  ))
    ;
    ;(testing "Decrease an articles count in the db"
    ;  (let [count-article (:count (get-get-first))]
    ;    (articles/dec! 0)
    ;    (is (= (dec count-article)
    ;           (:count (get-get-first))))))
    ;
    ;(testing "Set articles count to 0"
    ;  (let [count-article (:count (get-first))]
    ;    (articles/inc! 0)
    ;    (is (< count-article
    ;           (:count (get-first))))
    ;    (articles/rem! 0)
    ;    (is (= count-article
    ;           (:count (get-first))))))
    ;
    ;(testing "Remove an article in the db"
    ;  (let [count-article (:count (get-first-latest))]
    ;    (articles/delete! 0)
    ;    (is (= (dec count-article)
    ;           (count (articles/query-all))))))
    ))