(ns kulive.timetable
  (:require [re-frame.core :as re-frame]))


;; Refactor as macro...
(defn ttable-row-1
  []
  (fn []
    [:tr
     [:th 1] [:td]]))

(defn timetable-component
  "take a list of courses, get their times, put into timetable"
  []
  (let [courses (re-frame/subscribe [:courses])
        my-courses (re-frame/subscribe [:my-courses])
        my-course-ids (map first my-courses)
        get-schedule (fn [cid] (get-in courses [cid :en :schedule]))]
    (fn []
      [:h5 "Class Schedule"
       [:table.u-full-width
        [:thead
         [:tr
          [:th] [:th "Mon"] [:th "Tue"] [:th "Wed"] [:th "Thu"] [:th "Fri"]]]
        [:tbody
         [ttable-row-1]
         ;; [ttable-row-2]
         ;; [ttable-row-3]
         ;; [ttable-row-4]
         ;; [ttable-row-5]
         ;; [ttable-row-6]
         ;; [ttable-row-7]
         ;; [ttable-row-8]
         ;; [ttable-row-9]
         ]]])))
