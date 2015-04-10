(ns kulive.core
  (:require [kulive.model]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as str])
  (:require-macros [reagent.ratom  :refer [reaction]])
  (:import goog.History))

;; Views -------------------------

(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:form {:style {:margin-bottom "1rem"}}
       [:div.row
        [:div {:class "five columns"}
         [:label {:for "search-courses"} "Course Search"]
         [:input.u-full-width
          {:id "search-courses"
           :type "search"
           :on-change #(re-frame/dispatch
                        [:search-input-entered (-> % .-target .-value)])
           :placeholder "Course ID, Title, Professor, etc..."}]]]])))

(defn course-component
  "Individual course in search result list"
  [course]
  [:li [:a (kulive.model/course-hm->str course)]])

(defn courses-component []
  (let [courses (re-frame/subscribe [:filtered-courses])]
    (fn []
      [:div.row
       [:div {:class "u-full-width"}
        [:ul
         (for [course @courses]
           ^{:key (key course)} [course-component course])]]])))

(defn course-row-component [course]
  [:tr
   [:td (get-in (val course) [:c-id :kr :name])]
   [:td (get-in (val course) [:c-id :kr :number])]
   [:td (get-in (val course) [:c-id :kr :professor])]
   [:td (get-in (val course) [:c-id :kr :schedule])]
   [:td (get-in (val course) [:c-id :kr :credit-hours])]
   [:td (get-in (val course) [:c-id :kr :classification])]])

(defn courses-table-component []
  (let [courses (re-frame/subscribe [:filtered-courses])]
    (fn []
      [:table.u-full-width
       [:thead [:tr
                [:th "Name"]
                [:th "ID"]
                [:th "Professor"]
                [:th "Schedule"]
                [:th "Credits"]
                [:th "Type"]]]
       [:tbody
        (for [course @courses]
          ^{:key (:number course)} [course-row-component course])]])))

(defn timetable-component
  "take a list of courses, get their times, put into timetable"
  [course-ids]
  (let [courses (re-frame/subscribe [:courses])
        get-time (fn [cid] (get-in courses [cid :en :schedule]))]
    (fn []
      [:h5 "Class Schedule"
       [:table.u-full-width
        [:thead
         [:tr
          [:th] [:th "Mon"] [:th "Tue"] [:th "Wed"] [:th "Thu"] [:th "Fri"]]]
        [:tbody
         (for [time-slot (range 1 8)]
           ^{:key (hash time-slot)} [:tr
                                     [:th time-slot]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]])]]])))

(defn home-page []
  [:div.container
   [:div.row
    [:div {:course "nine column" :style {:margin-top "2%"
                                         :margin-bottom "5%"}}
     [:h2 "KU Live"]
     [search-component]
     ;;[courses-table-component]
     [courses-component]
     [timetable-component]
     [:div.row [:a {:href "#/about"} "about"]]]]])

(defn about-page []
  [:div [:a {:href "#/"} "home"]])

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
