(ns metro.components.web.views
  (:require [ring.util.response :as ring-resp]
            [metro.components.db.articles :as article]
            [metro.components.web.blocks :as blocks]))

(defn home-page [request]
  (blocks/base-template
   [:h1.center "Amazing Web Shop Application"]
   (blocks/checkout-btn)
   [:hr {:style "margin: 2rem 0"}]
   [:div.row.center {:style "margin: 0; padding-left: 1em;"}
    (vec (conj (blocks/article->big-cards) :tbody))]))

(defn checkout [request]
  (blocks/base-template
   [:h1.center "Checkout"]
   (if (article/has-articles-with-data?)
     [:table.table.table-striped.table-hover {:font-size ""}
      [:thead
       [:tr
        [:th {:width "20%"} "Category"]
        [:th {:width "50%"} "Article"]
        [:th {:width "10%"} "Image"]
        [:th {:width "10%"} "Count"]
        [:th {:width "10%"} "Remove"]]]
      (vec (conj (blocks/article->checkout) :tbody))]
     [:div
      [:h2.text-warning "Empty shopping cart! Manager cart is not amused!"]
      (blocks/🐱)])
   (blocks/buy-more-btn)))

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (ring-resp/redirect "/"))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (ring-resp/redirect "/"))

(defn rem-article [{:keys [form-params]}]
  (article/rem! (:id form-params))
  (ring-resp/redirect "/checkout"))

(defn respond-hello [request]
  (let [name (get-in request [:query-params :name])
        resp (cond (empty? name) "Hello, world!"
                   :else (str "Hello, " name "!"))]
    {:status 200 :body resp}))