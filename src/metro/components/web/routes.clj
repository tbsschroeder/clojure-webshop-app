(ns metro.components.web.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [metro.components.web.views :as views]
            [io.pedestal.http.route :as route]))

(def common-interceptors [(body-params/body-params) http/html-body])

(def routes
  #{["/" :get (conj common-interceptors `views/shop) :route-name :index]
    ["/checkout" :get (conj common-interceptors `views/checkout) :route-name :checkout]
    ["/article/inc" :post (conj common-interceptors `views/inc-article) :route-name :inc-article]
    ["/article/dec" :post (conj common-interceptors `views/dec-article) :route-name :dec-article]
    ["/article/rem" :post (conj common-interceptors `views/rem-article) :route-name :rem-article]
    ["/greet" :get views/respond-hello :route-name :greet]})

(def url-for (route/url-for-routes
              (route/expand-routes routes)))
