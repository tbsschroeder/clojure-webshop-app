(ns metro.components.web.api
  (:require [clojure.data.json :as json]
            [ring.util.response :as ring-resp]
            [metro.components.db.articles :as article]))

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (ring-resp/response ""))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (ring-resp/response ""))

(defn rem-article [{:keys [form-params]}]
  (article/rem! (:id form-params))
  (ring-resp/response ""))

(defn basket [_]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (article/query-all-with-count))})