(ns metro.components.web.api
  (:require [clojure.data.json :as json]
            [ring.util.response :refer [response]]
            [metro.components.db.articles :as article]))


(defn all-article [_]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (sort-by :title (article/query-all)))})

(defn basket [_]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (article/query-all-with-count))})

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (basket ""))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (basket ""))

(defn rem-article [{:keys [form-params]}]
  (article/rem! (:id form-params))
  (basket ""))