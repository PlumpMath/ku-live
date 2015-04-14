(ns kulive.view
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [kulive.model]
            [kulive.timetable :refer [timetable-component]]))


(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])
        course-count (re-frame/subscribe [:count-courses-in-search])]
    (fn []
      [:form {:style {:margin-bottom "1rem"}}
       [:div.row
        [:div {:class "six columns"}
         [:label {:for "search-courses"} (str "Course Search (" @course-count ")")]
         [:input.u-full-width
          {:id "search-courses"
           :type "search"
           :autoComplete "off"
           :on-change #(re-frame/dispatch
                        [:search-input-entered (-> % .-target .-value)])
           :on-key-down #(if (= (.-which %) 13)
                           (do (re-frame/dispatch
                                [:course-search-entered])
                               (.preventDefault %))
                           nil)
           :placeholder "Course ID, Title, Professor, etc..."}]]]])))

(defn course-component
  "Individual course in search result list"
  [course]
  [:li [:a (kulive.model/course-hm->str course)]])

(defn courses-component []
  (let [courses (re-frame/subscribe [:courses-to-display])]
    (fn []
      [:div.row
       [:div {:class "u-full-width"}
        [:ul
         (for [course @courses]
           ^{:key (key course)} [course-component course])]]])))

(defn course-row-component [course]
<<<<<<< HEAD
  [:tr
   [:td (get-in (val course) [:kr :name])]
   [:td (get-in (val course) [:kr :number])]
   [:td (get-in (val course) [:kr :professor])]
   [:td (str/join " " (get-in (val course) [:en :schedule]))]
   [:td (get-in (val course) [:kr :credit-hours])]
   [:td (get-in (val course) [:kr :classification])]])
=======
  (let [search-tokens (re-frame/subscribe [:search-tokens])]
    (fn []
      [:tr
       [:td (get-in (val course) [:kr :name])]
       [:td (get-in (val course) [:kr :number])]
       [:td (get-in (val course) [:kr :professor])]
       [:td (str/join " " (get-in (val course) [:kr :schedule]))]
       [:td (str/replace (get-in (val course) [:kr :credit-hours]) " " "")]
       [:td (get-in (val course) [:kr :classification])]])))
>>>>>>> Stuff

(defn courses-table-component []
  (let [courses-to-display (re-frame/subscribe [:courses-to-display])]
    (fn []
      [:div [:table.u-full-width
             [:thead [:tr
                      [:th "Name"]
                      [:th "ID"]
                      [:th "Professor"]
                      [:th "Schedule"]
                      [:th "Credits"]
                      [:th "Class"]]]
             [:tbody (if (= 1 (count @courses-to-display))
                       {:style {:background "#f5f6f0"}})
              (for [course @courses-to-display]
                ^{:key (get-in (val course) [:kr :number])}
                [course-row-component course])]
             (if (= 0 (count @courses-to-display))
               [:p.test "No classes to show"])]])))

(defn my-courses-component []
  (let [my-courses (re-frame/subscribe [:my-courses])]
    (fn []
      (if (empty? @my-courses)
        [:p "No courses yet"]
        [:p (str/join " " @my-courses)]))))

(defn home-page []
  [:div.container
   [:div.row
    [:div {:course "nine column" :style {:margin-top "2%"
                                         :margin-bottom "5%"}}
     [:h1 "KU Live"]
     [search-component]
     ;; [courses-component]
     [courses-table-component]
     [:h4 "My Courses"]
     [my-courses-component]
     [timetable-component]
     [:div.row [:a {:href "#/about"} "about"]]]]])

(defn about-page []
  [:div [:a {:href "#/"} "home"]])
