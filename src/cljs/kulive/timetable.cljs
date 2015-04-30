(ns kulive.timetable
  (:require [re-frame.core :as re-frame]))

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
         (for [time-slot (range 1 10)]
           ^{:key (hash time-slot)} [:tr
                                     [:th time-slot]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]
                                     [:td ""]])]]])))
