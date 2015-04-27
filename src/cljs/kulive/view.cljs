(ns kulive.view
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [kulive.model]
            [kulive.timetable :refer [timetable-component]]))


(defn search-component
  []
  (let [search-input (re-frame/subscribe [:search-input])
        course-count (re-frame/subscribe [:count-courses-in-search])
        displayed-courses (re-frame/subscribe [:courses-to-display])]
    (fn []
      [:form {:style {:margin-bottom "1rem"}}
       [:label {:for "search-courses"} (str "Course Search (" @course-count ")")]
       [:input
        {:id "search-courses"
         :type "search"
         :style {:width "50%"}
         :autoComplete "off"
         :on-change #(re-frame/dispatch
                      [:search-input-entered (-> % .-target .-value)])
         :on-key-down #(if (= (.-which %) 13)
                         (do (re-frame/dispatch
                              [:course-search-entered])
                             (.preventDefault %))
                         nil)
         :placeholder "Course ID, Title, Professor, etc..."}]
       (if (= 1 (count @displayed-courses))
         [:button.button-primary
          {:style {:margin-left ".8rem" :margin-botton "15px"}} "Add course"])])))

(defn course-row-component [course]
  "Row with course info"
  (let [search-tokens (re-frame/subscribe [:search-tokens])]
    (fn []
      [:tr
       [:td (get-in (val course) [:kr :name])]
       [:td (get-in (val course) [:kr :number])]
       [:td (get-in (val course) [:kr :professor])]
       [:td (str/join " " (get-in (val course) [:kr :schedule]))]
       [:td (str/replace (get-in (val course) [:kr :credit-hours]) " " "")]
       [:td (get-in (val course) [:kr :classification])]])))

(defn courses-table-component []
  "Table of course search results"
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
                       {:style {:background "#f4faff"}})
              (for [course @courses-to-display]
                ^{:key (get-in (val course) [:kr :number])}
                [course-row-component course])]
             (if (= 0 (count @courses-to-display))
               [:p.test "No classes to show"])]])))

(defn my-courses-component []
  "List of my selected courses"
  (let [my-courses (re-frame/subscribe [:my-courses])
        credit-sum (re-frame/subscribe [:sum-credits-in-my-courses])]
    (fn []
      [:div
       [:div
        [:h4 {:style {:display "inline-block"
                      :margin-right "2rem"}} "My Courses"]
        [:label {:style {:display "inline-block"}}
         "Total credits: " (str @credit-sum)]]
       (if (empty? @my-courses)
         [:p "No courses yet"]
         [:div
          [:ul (for [course @my-courses]
                 ^{:key (hash (first course))} [:li (str/join " " course)])]])])))

(defn home-page []
  [:div.container
   [:div {:style {:margin-top "4%" :margin-bottom "5%"}}
    [:h2 "KU_LIVE"]
    [search-component]
    [courses-table-component]
    [my-courses-component]
    [timetable-component]
    [:div.row [:a {:href "#/about"} "about"]]]])

(defn about-page []
  [:div.container {:style {:margin-top "4%"}}
   [:h2 "About"]
   [:p "Built with Clojure by Sooheon Kim"]
   [:a {:href "#/"} "home"]])
