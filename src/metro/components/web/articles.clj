(ns metro.components.web.articles
  (:require [korma.core :as kc]))

(kc/defentity article
  (kc/table :articles)
  (kc/entity-fields :id :title :image :count))

(defn create-table! []
  (kc/exec-raw "CREATE TABLE IF NOT EXISTS articles(id SERIAL, title TEXT NOT NULL, image TEXT NOT NULL, count INTEGER DEFAULT 0 NOT NULL)")
  (add! "Apples" "img/apples.jpeg" 0)
  (add! "Apricotes" "img/apricotes.jpeg" 0)
  (add! "Banana" "img/banana.jpeg" 0)
  (add! "Beef" "img/beef.jpeg" 0)
  (add! "Ben & Jerrys" "img/benjerrys.jpeg" 0)
  (add! "Fish" "img/fish.jpeg" 0)
  (add! "Pepper" "img/pepper.jpeg" 0)
  (add! "Rum" "img/rum.jpeg" 0)
  (add! "Salt" "img/salt.jpeg" 0)
  (add! "Water" "img/water.jpeg" 0))

(defn drop-table! []
  (kc/exec-raw "DROP TABLE IF EXISTS articles"))

(defn clear-table! []
  (kc/delete article))

(defn add! [title image count]
  (kc/insert article (kc/values {:title title
                                 :image image
                                 :count count})))

(defn delete! [id]
  (kc/delete article (kc/where {:id id})))

(defn toggle! [id]
  (kc/exec-raw (format "UPDATE articles SET done = NOT done WHERE id = %s" id)))

(defn inc! [id]
  (kc/exec-raw (format "UPDATE articles SET count = ISNULL(articles, 0) + 1 WHERE id = %s" id)))

(defn dec! [id]
  (kc/exec-raw (format "UPDATE articles SET count = ISNULL(articles, 0) - 1 WHERE id = %s" id)))

(defn query-all []
  (kc/select article))

(comment
  (create-table!)
  (add! "deleteme" "deleteit" 0)
  (delete! 10)
  (clear-table!))
