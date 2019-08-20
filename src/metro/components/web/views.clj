(ns metro.components.web.views
  (:require [ring.util.response :as ring-resp]
            [hiccup.page :as hp]
            [metro.components.db.articles :as article]))

(defn- count-view [article]
  [:div
   [:form#count-article {:action "/article/inc" :method :POST}
    [:input {:name "id" :value (:id article) :hidden true}]
    [:input {:class "btn btn-sm btn-success"
             :type  :submit
             :value "➕"
             :style "float: left"}]]
   [:form#count-article {:action "/article/dec" :method :POST}
    [:input {:name "id" :value (:id article) :hidden true}]
    [:input {:class "btn btn-sm btn-danger"
             :type  :submit
             :value "➖"
             :style "float: right"}]]
   ])

(defn- articles->rows []
  (for [article (sort-by :title (article/query-all))]
    [:tr
     [:td (:title article)]
     [:td [:img {:src (:image article) :style "height: 2em"}]]
     [:td [:strong {:class "text-info"} (:count article)]]
     [:td (count-view article)]]))

(defn- article-btn [action id class value]
  [:form#count-article {:action action :method :POST :style "float: right"}
   [:input {:name "id" :value id :hidden true}]
   [:input {:class (str "btn btn-sm " class)
            :type  :submit
            :value value}]])

(defn- article->cards []
  (for [article (sort-by :title (article/query-all))]
    [:div.card.text-white.bg-secondary.mb-3 {:style "max-width: 20rem; margin: 0.5rem;"}
     [:div.card-header (:title article)]
     [:div.card-body
      [:p.card-text (:description article)]
      [:img {:src (:image article) :style "height: 4em; float: left"}]
      [:h4.text-info {:style "padding: 0 5em;"} (:count article)]
      (article-btn "/article/dec" (:id article) "btn-danger" "➖")
      (article-btn "/article/inc" (:id article) "btn-success" "➕")]]))

;; -----------------------------------------------------------------------------

(defn base-template [& body]
  (ring-resp/response
   (hp/html5
    [:head
     [:title "Todo-Application"]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible"
             :content    "IE=edge"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1"}]
     (hp/include-css "css/bootstrap.min.css")]
    [:body
     [:div.container {:style "padding-top: 3rem"}
      body]])))

(defn home-page [request]
  (base-template
   [:h1.center "Amazing WebShop Application"]
   ;[:form#add-todo {:action "/article/add" :method :POST}
   ;
   ; [:div.form-group
   ;  [:label#title "New Todo"]
   ;  [:input.form-control {:name "title" :required true}]]
   ; [:input {:class "btn btn-primary"
   ;          :type  :submit
   ;          :value "Add"}]]
   ;
   ;[:form#article-inc {:action "/article/add" :method :POST}
   ; [:div.form-group
   ;  [:label#title "Increase"]
   ;  [:input.form-control {:name "title" :required true}]]
   ; [:input {:class "btn btn-success"
   ;          :type  :submit
   ;          :value "+"}]]
   ;
   ;[:form#article-dec {:action "/article/add" :method :POST}
   ; [:div.form-group
   ;  [:label#title "Decrease"]
   ;  [:input.form-control {:name "title" :required true}]]
   ; [:input {:class "btn btn-danger"
   ;          :type  :submit
   ;          :value "-"}]]
   ;
   ;[:hr {:style "margin: 3rem 0"}]
   ;
   ;[:h4 "Shopping Cart"]
   ;[:table.table.table-striped.table-hover
   ; [:thead
   ;  [:tr
   ;   [:th {:width "70%"} "Description"]
   ;   [:th {:width "10%"} "Image"]
   ;   [:th {:width "10%"} "Count"]
   ;   [:th {:width "10%"}]]]
   ; (vec (conj (articles->rows) :tbody))]

   [:hr {:style "margin: 3rem 0"}]
   [:div.row.center {:style "margin: 0"}
    (vec (conj (article->cards) :tbody))]

   [:hr {:style "margin: 3rem 0"}]))

(defn inc-article [{:keys [form-params]}]
  (article/inc! (:id form-params))
  (ring-resp/redirect "/"))

(defn dec-article [{:keys [form-params]}]
  (article/dec! (:id form-params))
  (ring-resp/redirect "/"))

(defn add-article [{:keys [form-params]}]
  (article/add! (:title form-params) (:image form-params) (:count form-params)))

(defn respond-hello [request]
  {:status 200 :body "Hello, world!"})
