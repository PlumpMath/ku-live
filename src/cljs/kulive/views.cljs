(ns kulive.views
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [kulive.subs])
  (:require-macros [reagent.ratom :refer [reaction]]))


(defn search []
  (let [courses (re-frame/subscribe [:courses])
        course-count (reaction (count @courses))]
    (fn []
      [:form {:style {:margin-bottom "0rem"}}
       [:label "강의 검색 (" @course-count ")"]
       [:input.form-control {:type "search"
                             :field :text
                             :id :course-search
                             :placeholder "ex) '국제관 화(5) 전공필수'"
                             :style {:width "40%" :margin-bottom "1rem"}
                             :on-change #()
                             :on-key-down #()}]
       [:button.button-primary {:style {:margin-left "1rem"}
                                :on-click #()}
        "add course"]])))

(defn friend-source [text]
  (filter
   #(-> % (.toLowerCase %) (.indexOf text) (> -1))
   ["Alice" "Alan" "Bob" "Beth" "Jim" "Jane" "Kim" "Rob" "Zoe"]))

(defn typeahead []
  (fn []
    [:div {:field :typeahead
           :id :ta
           :data-source friend-source
           :input-class "form-control"
           :list-class "typeahead-list"
           :item-class "typeahead-item"
           :highlight-class "highlighted"}]))

(defn course-row [])

(defn courses-table []
  (let [courses (re-frame/subscribe [:courses])]
    (fn []
      [:div
       [:p (str (first @courses))]])))

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
    [typeahead]
    [courses-table]
    [my-courses]
    [timetable]]])
