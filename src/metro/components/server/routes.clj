(ns metro.components.server.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor :refer [interceptor]]
            [metro.components.web.health :as health]
            [metro.components.web.views :as views]
            [metro.components.web.api :as api]
            [io.pedestal.http.route :as route]))

(def common-interceptors
  ;; Parsing the body parameters according to the request’s content-type header.
  [(body-params/body-params) http/html-body])

(def routes
  ;; Routes are a set of different rules.
  #{["/" :get (conj common-interceptors `views/shop) :route-name :index]
    ["/greet" :get views/respond-hello :route-name :greet]
    ["/health" :get (conj common-interceptors `health/health) :route-name :health]
    ["/checkout" :get (conj common-interceptors `views/checkout) :route-name :checkout]
    ["/article/inc" :post (conj common-interceptors `views/inc-article) :route-name :inc-article]
    ["/article/dec" :post (conj common-interceptors `views/dec-article) :route-name :dec-article]
    ["/article/rem" :post (conj common-interceptors `views/rem-article) :route-name :rem-article]
    ["/api/greet" :get api/respond-hello :route-name :api-greet]
    ["/api/article/all" :get (conj common-interceptors `api/all-article) :route-name :api-all-article]
    ["/api/article/inc" :post (conj common-interceptors `api/inc-article) :route-name :api-inc-article]
    ["/api/article/dec" :post (conj common-interceptors `api/dec-article) :route-name :api-dec-article]
    ["/api/article/rem" :post (conj common-interceptors `api/rem-article) :route-name :api-rem-article]
    ["/api/basket" :get (conj common-interceptors `api/basket) :route-name :api-basket]})

(def url-for
  ;; Helper function for unit tests
  (route/url-for-routes (route/expand-routes routes)))
