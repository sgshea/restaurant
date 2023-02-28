(ns sgshea.restaurant.web.routes.pages
  (:require
   [sgshea.restaurant.web.middleware.exception :as exception]
   [sgshea.restaurant.web.pages.layout :as layout]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [sgshea.restaurant.web.routes.utils :as utils]
   [sgshea.restaurant.web.controllers.ingredients :as ingredients]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                     {:status 403
                      :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(defn home [{:keys [flash] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)]
    (layout/render request "home.html" {:ingredients (query-fn :get-ingredients {})
                                        :errors (:errors flash)})))

;; Routes
(defn page-routes [_opts]
  [["/" {:get home}]
   ["/create-ingredient" {:post ingredients/create-ingredient!}]])

(defn route-data [opts]
  (merge
   opts
   {:middleware 
    [;; Default middleware for pages
     (wrap-page-defaults)
     ;; query-params & form-params
     parameters/parameters-middleware
     ;; encoding response body
     muuntaja/format-response-middleware
     ;; exception handling
     exception/wrap-exception]}))

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer!)
  [base-path (route-data opts) (page-routes opts)])

