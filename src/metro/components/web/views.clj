(ns metro.components.web.views
  (:require [ring.util.response :as ring-resp]
            [metro.components.db.articles :as article]
            [metro.components.web.api :as api]
            [metro.components.web.blocks :as blocks]))

(defn shop [_]
  (blocks/base-template
    [:h1.center "Amazing Web Shop Application"]
    (blocks/button->checkout)
    [:hr {:style "margin: 2rem 0"}]
    [:div.row {:style "margin: 0; padding-left: 1em;"}
     (vec (conj (blocks/article->big-cards) :tbody))]
    (blocks/text->pizza)))

(defn checkout [_]
  (blocks/base-template
    [:h1.center "Checkout"]
    (blocks/checkout-table)
    (blocks/button->buy-more)
    (blocks/text->pizza)))

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (ring-resp/redirect "/"))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (ring-resp/redirect "/"))

(defn rem-article [request]
  (api/rem-article request)
  (ring-resp/redirect "/checkout"))

(defn respond-hello [request]
  (let [name (get-in request [:query-params :name])
        resp (cond (empty? name) "Hello, world!"
                   :else (str "Hello, " name "!"))]
    {:status 200 :body resp}))