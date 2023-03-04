(ns sgshea.restaurant.core
    (:require
     [reagent.core :as r]
     [reagent.dom :as d]
     [ajax.core :refer [GET POST]]))

;; -------------------------
;; Resusable Components
(defn input-text 
  [val]
  [:input {:type :text
           :value val
           :on-change #(reset! val (-> % .-target .-value))}])

;; -------------------------
;; Views
(defn handler [response]
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console
    (str "something bad happened: " status " " status-text)))

(defonce ingredient-name
  (r/atom ""))
(defonce ingredient-amount
  (r/atom 0))

(defn name-input []
  [:input {:type "text"
           :value @ingredient-name
           :on-change #(reset! ingredient-name (-> % .-target .-value))}])
(defn amount-input []
  [:input {:type "number"
           :value @ingredient-amount
           :on-change #(reset! ingredient-amount (-> % .-target .-value))}])

;; GET health
(defn get-health []
  (GET "/api/health" {:handler handler}))
(defn get-health-button []
  [:input {:type "button"
           :value "Get Health"
           :on-click get-health}])

(defn get-ingredients []
  (GET "/api/ingredients" {:handler handler}))
(defn get-ingredients-button []
  [:input {:type "button"
           :value "Get Ingredients"
           :on-click get-ingredients}])

(defn home-page []
  [:div
   [:p "name is " @ingredient-name]
   [name-input]
   [:p "amount is " @ingredient-amount]
   [amount-input]
   [:p "Get ingredients (to log)"]
   [get-ingredients-button]
   [get-health-button]
   ])

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
