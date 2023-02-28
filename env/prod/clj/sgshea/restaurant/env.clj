(ns sgshea.restaurant.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[sgshea.restaurant starting]=-"))
   :start      (fn []
                 (log/info "\n-=[sgshea.restaurant started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[sgshea.restaurant has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
