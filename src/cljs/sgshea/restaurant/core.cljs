(ns sgshea.restaurant.core
    (:require
     [reagent.core :as r]
     [reagent.dom :as d]
     [reitit.core :as reitit]
     [goog.events :as events]
     [goog.history.EventType :as HistoryEventType])
  (:import goog.History))

;; -------------------------
;; Views

(def session (r/atom {:page :home}))

(defn home-page []
  [:div "Home"])

(defn about-page []
  [:div "About"])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [(pages (:page @session))])

(def router
  (reitit/router
   [["/" :home]
    ["about" :about]]))

(defn match-route [uri]
  (->> (or (not-empty (string/replace uri #"^.*#" "")) "/")
       (reitit/match-by-path router)
       :data
       :name))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (swap! session assoc :page (match-route (.-token event)))))
    (.setEnabled true)))
;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
