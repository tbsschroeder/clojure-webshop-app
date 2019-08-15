(ns metro.components.web.views
  (:require [ring.util.response :as ring-resp]
            [hiccup.page :as hp]
            [metro.components.web.articles :as articles]))

(defn- toggle-todo-view [todo]
  [:form#toggle-todo {:action "/todo/toggle" :method :POST}
   [:input {:name "id" :value (:id todo) :hidden true}]
   [:input {:class "btn btn-sm btn-primary"
            :type  :submit
            :value "Toggle"}]])

(defn- articles->rows []
  (for [todo (sort-by :title (articles/query-all))]
    [:tr
     [:td (:title todo)]
     [:td (if (:done todo) "✅" "❌")]
     [:td (toggle-todo-view todo)]]))

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
   [:h1 "WebShop Application"]
   [:form#add-todo {:action "/article/add" :method :POST}
    [:div.form-group
     [:label#title "New Todo"]
     [:input.form-control {:name "title" :required true}]]
    [:input {:class "btn btn-primary"
             :type  :submit
             :value "Add"}]]

   [:form#article-inc {:action "/article/add" :method :POST}
    [:div.form-group
     [:label#title "+"]
     [:input.form-control {:name "title" :required true}]]
    [:input {:class "btn btn-success"
             :type  :submit
             :value "inc"}]]

   [:form#article-dec {:action "/article/add" :method :POST}
    [:div.form-group
     [:label#title "-"]
     [:input.form-control {:name "title" :required true}]]
    [:input {:class "btn btn-warning"
             :type  :submit
             :value "dec"}]]

   [:hr {:style "margin: 3rem 0"}]

   [:h4 "articles"]
   [:table.table.table-striped
    [:thead
     [:tr
      [:th {:width "90%"} "Title"]
      [:th "Done?"]
      [:th]]]
    (vec (conj (articles->rows) :tbody))]))

(defn inc-article [{:keys [form-params]}]
  (articles/inc! form-params)
  (ring-resp/redirect "/"))

(defn dec-article [{:keys [form-params]}]
  (articles/dec! form-params)
  (ring-resp/redirect "/"))

(defn add-article [{:keys [form-params]}]
  (article/add! (:title form-params) (:image form-params) (:count form-params))

  (defn toggle-todo [{:keys [form-params]}]
    (articles/toggle! (:id form-params))
    (ring-resp/redirect "/"))

  (defn respond-hello [request]
    {:status 200 :body "Hello, world!"}))
