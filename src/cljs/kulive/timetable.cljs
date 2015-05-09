(ns kulive.timetable
  (:require [re-frame.core :as re-frame]))


(defn ttable-row
  [time-period]
  (let [my-schedule (re-frame/subscribe [:my-schedule])]
    (fn []
      (println "hello")
      [:tr [:th time-period]
       (doall (for [day ["Mon" "Tue" "Wed" "Thu" "Fri"]]
                ^{:key {day time-period}}
                [:td (get @my-schedule [day time-period])]))])))

(defn timetable-component
  "take a list of courses, get their times, put into timetable"
  []
  (fn []
    [:h5 "Class Schedule"
     [:table.u-full-width
      [:thead
       [:tr
        [:th] [:th "Mon"] [:th "Tue"] [:th "Wed"] [:th "Thu"] [:th "Fri"]]]
      [:tbody
       (for [time (range 1 9)]
         ^{:key time}
         [ttable-row time])
       ]]]))
