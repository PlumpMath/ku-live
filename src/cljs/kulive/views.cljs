(ns kulive.views
  (:require [kulive.handlers]
            [kulive.subs]
            [kulive.typeahead :refer [typeahead]]
            [re-frame.core :as rf])
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

(defn course-row [course]
  "Row with course info")

(defn courses-table []
  "Table of course search results"
  (let [courses (rf/subscribe [:courses])]
    (fn []
      [:div [:table.u-full-width
             [:thead [:tr
                      [:th "과목명"]
                      [:th "ID"]
                      [:th "교수"]
                      [:th "강의시간"]
                      [:th "구분"]]]
             ;; [:tbody (for [course @courses]
             ;;           ^{:key (get-in (val course) [:kr :number])}
             ;;           [course-row-component course])]
             ]])))

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
