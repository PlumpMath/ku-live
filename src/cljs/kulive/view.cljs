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
        displayed-courses (re-frame/subscribe [:courses-to-display])
        add-course #(do (re-frame/dispatch [:course-search-entered])
                        (.preventDefault %))]
    (fn []
      [:form {:style {:margin-bottom "0rem"}}
       [:label
        (str "강의 검색 (" @course-count ")")]
       [:input
        {:type "text"
         :id "search-courses"
         :style {:width "42%"
                 :margin-bottom "1rem"}
         :autoComplete "off"
         :on-change #(re-frame/dispatch
                      [:search-input-entered (-> % .-target .-value)])
         :on-key-down #(if (= (.-which %) 13)
                         add-course
                         nil)
         :placeholder "ex) \"국제관 화(5) major required\""}]
       (if (= 1 (count @displayed-courses))
         [:button.button-primary
          {:style {:margin-left ".8rem" :margin-bottom "1rem"}
           :on-click add-course}
          "Add course"])])))

(defn course-row-component [course]
  "Row with course info"
  (let [courses-to-display (re-frame/subscribe [:courses-to-display])]
    (fn []
      [:tr (if (= 1 (count @courses-to-display))
             {:style {:background "#f4faff"}})
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
      [:table {:style {:height "25%"
                       :width "100%"
                       :margin-bottom "6rem"}}
       [:thead.course-table
        [:tr
         [:th "과목명"]
         [:th "ID"]
         [:th "담당교수"]
         [:th "강의시간"]
         [:th "학점"]
         [:th "구분"]]]
       [:tbody.course-table
        (for [course @courses-to-display]
          ^{:key (get-in (val course) [:kr :number])}
          [course-row-component course])]
       (if (= 0 (count @courses-to-display))
         [:p.test "No classes to show"])])))

(defn my-course-component [course]
  [:div
   [:li {:style {:display "inline-block"
                 :margin-right "1.5rem"}} (str/join " " course)]
   [:a "Drop"]])

(defn my-courses-component []
  "List of my selected courses"
  (let [my-courses (re-frame/subscribe [:my-courses])
        credit-sum (re-frame/subscribe [:sum-credits-in-my-courses])]
    (fn []
      [:div
       [:div
        [:h4 {:style {:display "inline-block"
                      :margin-right "2rem"}} "My Courses"]
        [:label {:style {:display "inline-block"
                         :margin-right "1.5rem"}}
         "Total credits: " (str @credit-sum)]
        (if (not (empty? @my-courses)) [:a "Drop All"])]
       (if (empty? @my-courses)
         [:p "No courses yet"]
         [:div
          [:ul (for [course @my-courses]
                 ^{:key (hash (first course))} [my-course-component course])]])])))

(defn home-page []
  [:div.container
   [:h3 {:style {:margin-bottom "0rem"}} "KULIVE"]
   [:div {:style {:padding "10px"}}
    [search-component]
    [courses-table-component]
    [my-courses-component]
    [timetable-component]
    [:div.row [:a {:href "#/about"} "about"]]]])

(defn about-page []
  [:div.container
   [:h2 "About"]
   [:p "Built with Clojure by Sooheon Kim"]
   [:a {:href "#/"} "home"]])
