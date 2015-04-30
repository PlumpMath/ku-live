(ns kulive.core
  (:require [kulive.view :refer [home-page about-page]]
            [kulive.handlers]
            [kulive.subscribers]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as str])
  (:require-macros [reagent.ratom  :refer [reaction]])
  (:import goog.History))

(defn current-page []
  [:div [(session/get :current-page)]])

;; Routes -------------------------

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; History -------------------------

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; Initialise app -------------------------

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (re-frame/dispatch-sync [:initialise-db])
  (mount-root))
