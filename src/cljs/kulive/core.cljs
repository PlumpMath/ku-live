(ns kulive.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :as re-frame]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljsjs.react :as react]
            [clojure.string :as s])
  (:require-macros [reagent.ratom  :refer [reaction]])
  (:import goog.History))

;; Subscriptions & handlers -------------------------

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

#_(def db
    (clojure.edn/read-string (slurp "resources/db/db2015-1R.edn")))

(re-frame/register-handler
 :initialise-db
 (fn
   [_ _]
   {:courses [{:name "Beginner CSS" :number "CSS101" :time "Mon(2) Wed(2)"
               :credits "3" :type "Elective" :prof "Prof. A"}
              {:name "Intermediate CSS" :number "CSS201" :time "Tue(5) Thu(5)"
               :credits "3" :type "Elective" :prof "Prof. B"}
              {:name "Advanced Javascript" :number "JS406" :time "Mon(6) Wed(6)"
               :credits "3" :type "Major" :prof "Prof. C"}
              {:name "ClojureScript" :number "CLJS303" :time "Fri(1,2)"
               :credits "3" :type "Major" :prof "Prof. D"}
              {:name "Clojure" :number "CLJ505" :time "Tue(3,4) Thu(3,4)"
               :credits "3" :type "Major" :prof "Prof. D"}
              {:name "How to Train Your Dragon" :number "DR505" :prof "Prof. E"
               :time "Tue(7) Thu(7)" :credits "3" :type "Elective"}]
    :search-input ""}))

(defn handle-search-input-entered
  [app-state [_ search-input]]
  (assoc-in app-state [:search-input] search-input))

(re-frame/register-handler
 :search-input-entered
 handle-search-input-entered)

;; Views -------------------------

(defn matches-query?
  [search-input course]
  (let [matches-input? (fn [key]
                         (re-find (re-pattern (s/lower-case search-input))
                                  (s/lower-case (key course))))]
    (if (= "" search-input)
      true
      (or (matches-input? :name)
          (matches-input? :number)
          (matches-input? :prof)
          (matches-input? :time)
          (matches-input? :credits)
          (matches-input? :type)
          ))))

(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:div
       [:div "Search for course, professor, date..."]
       [:input
        {:on-change #(re-frame/dispatch
                      [:search-input-entered (-> % .-target .-value)])
         :placeholder ""}]])))

(defn course-component
  "Individual course in search result list"
  [course]
  [:li [:a (interpose ", " [(:name course)
                            (:number course)
                            (:prof course)
                            (:time course)
                            (:credits course)
                            (:type course)])]])

(defn courses-component
  []
  (let [courses (re-frame/subscribe [:courses])
        search-input (re-frame/subscribe [:search-input])]
    (fn []
      [:div
       [:ul
        (for [course (filter (partial matches-query? @search-input) @courses)]
          ^{:key (:number course)} [course-component course])]])))

(defn timetable-component
  "take a list of courses, get their times, put into timetable"
  [course-ids]
  (let [get-time (fn [cid] (get-in app-state))]))

(defn home-page []
  [:div.container
   [:div.row
    [:div {:course "six column"
           :style {:margin-top "10%"}}
     [:h2 "KU Live"]
     [search-component]
     [courses-component]
     [:div [:a {:href "#/about"} "about"]]]]])

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

;; Initialize app -------------------------

(defn init! [] (hook-browser-navigation!)
  (re-frame/dispatch [:initialise-db])
  (reagent/render [current-page] (.getElementById js/document "app")))
