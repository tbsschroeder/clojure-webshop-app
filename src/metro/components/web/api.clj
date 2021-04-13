(ns metro.components.web.api
  (:require [clojure.data.json :as json]
            [ring.util.response :refer [response]]
            [metro.components.db.articles :as article]))

(defn respond-hello [request]
  (let [name (get-in request [:query-params :name])
        resp (if (empty? name) "Hello, world!" (str "Hello, " name "!"))]
    {:status 200 :body resp}))


(defn all-article [_]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:articles (sort-by :title (article/query-all))})})

(defn basket [_]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:articles (article/query-all-with-count)})})

(defn- article [id]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:id id
                             :articles (article/query-all)})})

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (article (:id form-params)))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (article (:id form-params)))

(defn rem-article [{:keys [form-params]}]
  (article/rem! (:id form-params))
  (all-article ""))
