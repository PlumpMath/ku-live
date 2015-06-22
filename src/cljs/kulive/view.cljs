(ns kulive.view
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [kulive.timetable :refer [timetable-component]])
  (:require-macros [reagent.ratom :refer [reaction]]))


(defn search-component
  []
  (let [course-count (re-frame/subscribe [:count-courses-in-search])
        displayed-courses (re-frame/subscribe [:courses-to-display])
        display-count (reaction (count @displayed-courses))]
    (fn []
      [:form {:style {:margin-bottom "0rem"}}
       [:label (str "강의 검색 (" @course-count ")")]
       [:input {:type "search"
                :id "search-courses"
                :style {:width "42%"
                        :margin-bottom "1rem"}
                :autoComplete "off"
                :on-change #(re-frame/dispatch
                             [:search-input-entered (-> % .-target .-value)])
                :on-key-down #(if (= (.-which %) 13)
                                (do (re-frame/dispatch [:course-added])
                                    (.preventDefault %))
                                nil)
                :placeholder "ex) \"국제관 화(5) major required\""}]
       (if (= 1 @display-count)
         [:button.button-primary
          {:style {:margin-left "1rem" :margin-bottom "1rem"}
           :on-click #(do (re-frame/dispatch [:course-added])
                          (.preventDefault %))}
          "Add course"])])))

(defn course-row-component [course]
  "Row with course info"
  (let [courses-to-display (re-frame/subscribe [:courses-to-display])
        display-count (reaction (count @courses-to-display))]
    (fn []
      [:tr {:style {:cursor "pointer"
                    :background (if (= 1 @display-count)
                                  "#f4faff")}}
       [:td (apply str (rest (get-in (val course) [:kr :name])))]
       [:td (get-in (val course) [:kr :number])]
       [:td (get-in (val course) [:kr :professor])]
       [:td (str/join (get-in (val course) [:kr :schedule]))]
       [:td (str/replace (get-in (val course) [:kr :credit-hours]) " " "")]
       [:td (get-in (val course) [:kr :classification])]])))

(defn courses-table-component []
  "Table of course search results"
  (let [courses-to-display (re-frame/subscribe [:courses-to-display])]
    (fn []
      [:div
       [:table.u-full-width
        [:thead
         [:tr
          [:th "과목명"]
          [:th "ID"]
          [:th "담당교수"]
          [:th "강의시간"]
          [:th "학점"]
          [:th "구분"]]]
        [:tbody
         (for [course @courses-to-display]
           ^{:key (get-in (val course) [:kr :number])}
           [course-row-component course])]]
       (if (= 0 (count @courses-to-display))
         [:p.test "No classes to show"])])))

(defn drop-course [course]
  [:a {:href "#"
       :on-click #(do
                    (re-frame/dispatch [:course-dropped course])
                    (.preventDefault %))}
   "drop"])

(defn my-course-component [course]
  (let [courses (into {} @(re-frame/subscribe [:courses]))]
    (fn []
      [:div
       [:li
        {:style {:display "inline-block"
                 :margin-right "1rem"}
         :id course}
        (str/join " - " [course
                         (apply str (rest ; remove first space
                                     (get-in (courses course) [:kr :name])))
                         (get-in (courses course) [:kr :professor])])]
       [drop-course course]])))

(defn drop-all-courses []
  [:a {:href "#"
       :on-click #(do (re-frame/dispatch [:drop-all-courses])
                      (.preventDefault %))}
   "drop all"])

(defn my-courses-component []
  "List of my selected courses"
  (let [my-courses (re-frame/subscribe [:my-courses])
        credit-sum (re-frame/subscribe [:sum-credits-in-my-courses])]
    (fn []
      [:div
       [:div
        [:h4
         {:style {:display "inline-block"
                  :margin-right "2rem"}}
         "My Courses"]
        [:label
         {:style {:display "inline-block"
                  :margin-right "1.5rem"}}
         "Total credits: " (str @credit-sum)]
        (if (not (empty? @my-courses))
          [drop-all-courses])]
       (if (empty? @my-courses)
         [:p "No courses yet"]
         [:div
          [:ul
           (for [course @my-courses]
             ^{:key (hash (first course))}
             [my-course-component course])]])])))

(defn home-page []
  [:div.container
   [:h3 "KULIVE"]
   [:div
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
