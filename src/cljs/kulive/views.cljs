(ns kulive.views
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame])
  (:require-macros [reagent.ratom :refer [reaction]]))


(defn search []
  (let []
    (fn []
      [:form
       [:label "강의 검색"]
       [:input {:type "search"
                :id "course-search"
                :placeholder "ex) '국제관 화(5) 전공필수'"
                :style {:width "40%"}
                :autoComplete "off"
                :on-change #()}]
       [:button.button-primary {:style {:margin-left "1rem"}
                                :on-click #()}
        "add course"]])))

(defn course-row [])

(defn courses-table [])

(defn my-courses [])

(defn timetable [])

(defn home-page []
  [:div.container
   [:h3 "KULIVE"]
   [:div
    [search]
    [courses-table]
    [my-courses]
    [timetable]]])
