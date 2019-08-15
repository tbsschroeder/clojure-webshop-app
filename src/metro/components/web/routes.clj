(ns metro.components.web.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [metro.components.web.views :as views]
            [io.pedestal.http.route :as route]))

(def common-interceptors [(body-params/body-params) http/html-body])

(def routes
  #{["/" :get (conj common-interceptors `views/home-page) :route-name :index]
    ["/article/add" :post (conj common-interceptors `views/add-article) :route-name :add-article]
    ["/article/toggle" :post (conj common-interceptors `views/toggle-todo) :route-name :todo-toggle]
    ["/greet" :get views/respond-hello :route-name :greet]})

(def url-for (route/url-for-routes
              (route/expand-routes routes)))
