(ns sgshea.restaurant.web.controllers.ingredients
  (:require
   [clojure.tools.logging :as log]
   [sgshea.restaurant.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn create-ingredient!
  [{{:strs [name amount]} :form-params :as request}]
  (log/debug "saving ingredient" name amount)
  (let [{:keys [query-fn]} (utils/route-data request)]
    (try
      (if (or (empty? name) (empty? amount))
        (cond-> (http-response/found "/")
                (empty? name)
                (assoc-in [:flash :errors :name] "name is required")
                (empty? amount)
                (assoc-in [:flash :errors :name] "amount is required"))
        (do
          (query-fn :create-ingredient! {:name name :amount amount})
          (http-response/found "/")))
      (catch Exception e
        (log/error e "failed to create ingredient!")
        (-> (http-response/found "/")
            (assoc :flash {:errors {:unknown (.getMessage e)}})))))) 

(defn get-ingredients!
  [req]
  (log/debug "getting ingredients")
  (let [{:keys [query-fn]} (utils/route-data req)]
    (try
      (http-response/ok (query-fn :get-ingredients))
      (catch Exception e
        (log/error e "failed to get ingredients!")
        (-> (http-response/found "/"))))))