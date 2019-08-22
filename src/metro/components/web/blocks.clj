(ns metro.components.web.blocks
  (:require  [metro.components.db.articles :as article]
             [ring.util.response :as ring-resp]
             [hiccup.page :as hp]))

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
     (hp/include-css "css/bootstrap.min.css")
     (hp/include-css "css/cat.css")]
    [:body
     [:div.container {:style "padding-top: 3rem"}
      body]])))

(defn- article-btn [action id class value]
  [:form#count-article {:action action
                        :method :POST}
   [:input {:name "id"
            :value id
            :hidden true}]
   [:input {:class (str "btn " class)
            :type  :submit
            :value value}]])

(defn- count-input [article]
  [:div.form-group {:style "width: 10em!important; float: right; margin-top: 0.7em;"}
   [:div.input-group
    [:div.input-group-prepend
     (article-btn "/article/dec" (:id article) "btn-danger" "➖")]
    [:input.form-control.input-number {:value (:count article)
                                       :width "3em"
                                       :disabled "true"
                                       :style "text-align: center"}]
    [:div.input-group-append
     (article-btn "/article/inc" (:id article) "btn-success" "➕")]]])

(defn article->big-cards []
  (for [article (sort-by :title (article/query-all))]
    [:div.card.mb-3 {:style "max-width: 31%; margin: 0.5rem;"}
     [:h3.card-header (:title article)]
     [:div.card-body
      [:h6.card-subtitle.text-muted (:category article)]]
     [:img {:style "width: 100%; display: block;"
            :src (:image article)
            :alt "article image"}]
     [:div.card-body
      [:p.card-text (:description article)]
      [:a.card-link {:href "#"} "More"]
      (count-input article)]]))

(defn article->small-cards []
  (for [article (sort-by :title (article/query-all))]
    [:div.card.text-white.bg-secondary.mb-3 {:style "max-width: 31%; margin: 0.5rem;"}
     [:div.card-header [:h4 (:title article)]]
     [:div.card-body
      [:p.card-text (:description article)]
      [:img {:src (:image article) :style "height: 4em; float: left"}]
      (count-input article)]]))

(defn article->checkout []
  (for [article (sort-by (juxt :category :title) (article/query-all-with-count))]
    [:tr
     [:td (:category article)]
     [:td (:title article)]
     [:td [:img {:src (:image article) :style "height: 2em"}]]
     [:td [:strong {:class "text-warning"} (:count article)]]
     [:td (article-btn "/article/rem" (:id article) "btn-warning btn-sm" "✘")]]))

(defn checkout-btn []
  [:a.btn.btn-success
   {:type "button"
    :style "position: fixed; top: 0; right: 0;"
    :href "/checkout"}
   [:img {:src "img/shopping-cart.svg"
          :alt "cart"
          :style "margin-right: 0.5em; filter: invert(100%); height: 1em"}]
   (str "Checkout (" (count (article/query-all-with-count)) ")")])

(defn buy-more-btn []
  [:a.btn.btn-success
   {:type "button"
    :style "float: right"
    :href "/"}
   [:img {:src "img/shopping-cart.svg"
          :alt "cart"
          :style "margin-right: 0.5em; filter: invert(100%); height: 1em"}]
   "Buy More"])

(defn cat []
  [:div.cat
   [:div.head
    [:div.face
     [:div.stripes
      [:div.top]
      [:div.left]
      [:div.right]]
     [:div.eyes
      [:div.left]
      [:div.right]]
     [:div.nose]
     [:div.mouth]]
    [:div.ears
     [:div.left]
     [:div.right]]]
   [:div.suit
    [:div.collar
     [:div.top]]
    [:div.arms
     [:div.left]
     [:div.right]]
    [:div.paws
     [:div.left]
     [:div.right]]
    [:div.body]]
   [:div.tail]
   [:div.shadow]])