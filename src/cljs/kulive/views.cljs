(ns kulive.views
  (:require [re-frame.core :as rf]
            [kulive.subs]
            [kulive.handlers]
            [kulive.typeahead :refer [typeahead]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn search []
  (let [courses (rf/subscribe [:courses])
        course-count (reaction (count @courses))]
    (fn []
      [:form
       [:label "강의 검색 (" @course-count ")"]
       [:input {:type "search"
                :placeholder "ex) '국제관 화(5) 전공필수'"
                :style {:width "45%"}
                :on-change #()
                :on-key-down #()}]
       [:button.button-primary {:style {:margin-left "1rem"} :on-click #()}
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
    [typeahead]]])
