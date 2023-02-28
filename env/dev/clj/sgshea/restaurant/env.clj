(ns sgshea.restaurant.env
  (:require
    [clojure.tools.logging :as log]
    [sgshea.restaurant.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[sgshea.restaurant starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[sgshea.restaurant started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[sgshea.restaurant has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
