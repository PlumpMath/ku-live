(ns kulive.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljsjs.react :as react]
            [re-frame.core :as re-frame]
            [clojure.string :as s])
  (:require-macros [reagent.ratom  :refer [reaction]])
  (:import goog.History))

;; -------------------------
;; Subscriptions & handlers

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :classes
 (fn [db]
   (reaction (:classes @db)))) ;; pulls out :classes

(re-frame/register-handler
 :initialise-db ;; usage: (dispatch [:initialise-db])
 (fn
   [_ _] ;; Ignore both params (db and v).
   {:classes [{:name "Beginner CSS" :number "CSS101" :time "Mon(2) Wed(2)"
               :credits "3" :type "Elective"}
              {:name "Intermediate CSS" :number "CSS201" :time "Tue(5) Thu(5)"
               :credits "3" :type "Elective"}
              {:name "Advanced Javascript" :number "JS406" :time "Mon(6) Wed(6)"
               :credits "3" :type "Major"}
              {:name "ClojureScript" :number "CLJS303" :time "Fri(1,2)"
               :credits "3" :type "Major"}
              {:name "Clojure" :number "CLJ505" :time "Tue(3,4) Thu(3,4)"
               :credits "3" :type "Major"}
              {:name "How to Train Your Dragon" :number "Drag505"
               :time "Tue(7) Thu(7)" :credits "3" :type "Elective"}
              ]
    :search-input ""}))

(defn handle-search-input-entered
  [app-state [_ search-input]]
  (assoc-in app-state [:search-input] search-input))

(re-frame/register-handler
 :search-input-entered
 handle-search-input-entered)

;; -------------------------
;; Views

(defn class-component
  "Individual class in search result list"
  [class]
  [:li.list-group-item (:name class)
   [:span (:number class)] [:span (:time class)] [:span (:credits class) (:type class)]])

(defn matches-query?
  [search-input class]
  (let [lc s/lower-case
        match-input-to-key (fn [key] (re-find (re-pattern (lc search-input))
                                              (lc (key class))))]
    (if (= "" search-input)
      true
      (boolean (or (match-input-to-key :name)
                   (match-input-to-key :number)
                   (match-input-to-key :time)
                   (match-input-to-key :credits)
                   (match-input-to-key :name)
                   (match-input-to-key :type)
                   )))))

(defn classes-component
  []
  (let [classes (re-frame/subscribe [:classes])
        search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:div.panel.panel-default
       [:div.panel-heading [:h3.panel-title "Search Results"]]
       [:ul.list-group
        (for [class (filter (partial matches-query? @search-input) @classes)]
          ^{:key (:number class)} [class-component class])]])))

(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:div.t-center ;; Wrapper for search
       [:h3.panel-title "KU Live"
        [:input.main-search
         {:on-change #(re-frame/dispatch
                       [:search-input-entered (-> % .-target .-value)])
          :placeholder "Search for class, professor, etc..."}]]])))

(defn home-page []
  [:div
   [search-component]
   [classes-component]])

(defn about-page []
  [:div [:h2 "About kulive"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History ;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (re-frame/dispatch [:initialise-db])
  (mount-root))
