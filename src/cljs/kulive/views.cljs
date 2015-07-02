(ns kulive.views
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame])
  (:require-macros [reagent.ratom :refer [reaction]]))


(defn search []
  (let []
    (fn []
      [:form {:style {:margin-bottom "0rem"}}
       [:label "강의 검색"]
       [:input {:type "search"
                :id "course-search"
                :placeholder "ex) '국제관 화(5) 전공필수'"
                :style {:width "40%" :margin-bottom "1rem"}
                :autoComplete "off"
                :on-change #()
                :on-key-down #()}]
       [:button.button-primary {:style {:margin-left "1rem"}
                                :on-click #()}
        "add course"]])))

(defn course-row [])

(defn courses-table [])

(defn my-courses []
  (fn []
    [:div
     [:h4 {:style {:display "inline-block" :margin-right "1.5rem"}}
      "My Courses"]
     [:label {:style {:display "inline-block" :margin-right "1.5rem"}}
      "Total credits: [some number]"]]))

(defn timetable []
  (fn []
    [:div
     [:h4 "Timetable"]
     [:p "I'm a table"]]))

(defn home-page []
  [:div.container
   [:h3 "KULIVE"]
   [:div
    [search]
    [courses-table]
    [my-courses]
    [timetable]]])
