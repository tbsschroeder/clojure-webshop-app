(ns metro.components.web.views
  (:require [ring.util.response :as ring-resp]
            [hiccup.page :as hp]
            [metro.components.db.articles :as article]
            [metro.components.web.blocks :as blocks]))

(defn base-template [& body]
  (ring-resp/response
   (hp/html5
    [:head
     [:title "Webshop"]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible"
             :content    "IE=edge"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1"}]
     [:link {:rel "icon" :href "img/shopping-cart.svg"}]
     (hp/include-css "css/bootstrap.min.css")]
    [:body
     [:div.container {:style "padding-top: 3rem"}
      body]])))

(defn home-page [request]
  (base-template
    [:h1.center "Amazing Web Shop Application"]
    (blocks/checkout-btn)
    [:hr {:style "margin: 2rem 0"}]
    [:div.row.center {:style "margin: 0; padding-left: 1em;"}
     (vec (conj (blocks/article->big-cards) :tbody))]))

(defn checkout [request]
  (base-template
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
      [:h4.text-warning "Empty shopping cart"])
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
  {:status 200
   :body "Hello, world!"})