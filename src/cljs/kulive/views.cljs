(ns kulive.views
  (:require [re-frame.core :as rf]
            [kulive.subs]
            [kulive.handlers])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn value-of [element]
  (-> element .-target .-value))

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

(defn typeahead []
  (let [typeahead-db (rf/subscribe [:typeahead-db])
        value (reaction (:value @typeahead-db))
        data-source (reaction (filter #(-> %
                                           (.toLowerCase %)
                                           (.indexOf @value)
                                           (> -1))
                                      ["Alice" "Alan" "Bob" "Beth" "Jim" "Jane" "Kim" "Rob" "Zoe"]))
        selected-index (reaction (:selected-index @typeahead-db))
        selections (reaction (:selections @typeahead-db))
        typeahead-hidden? (reaction (:hidden @typeahead-db))
        mouse-on-list? (reaction (:mouse-on-list? @typeahead-db))]
    (fn []
      [:div
       [:input {:type "text"
                :style {:width "70%"}
                :on-blur #(when-not @mouse-on-list?
                            (rf/dispatch [:set-typeahead-hidden true])
                            (rf/dispatch [:set-typeahead-selected-index 0]))
                :on-change #(do
                              (rf/dispatch [:set-typeahead-selections @data-source])
                              (rf/dispatch [:set-typeahead-value (value-of %)])
                              (rf/dispatch [:set-typeahead-hidden false])
                              (rf/dispatch [:set-typeahead-selected-index 0]))
                :on-key-down #(do
                                (case (.-which %)
                                  ;; Up key
                                  38 (do (.preventDefault %)
                                         (if-not (= 0 @selected-index)
                                           (rf/dispatch [:set-typeahead-selected-index
                                                         (dec @selected-index)])))
                                  ;; Down key
                                  40 (do (.preventDefault %)
                                         (if-not (= @selected-index
                                                    (dec (count @selections)))
                                           (rf/dispatch [:set-typeahead-selected-index
                                                         (inc @selected-index)])))
                                  ;; Enter key
                                  13 (do
                                       (rf/dispatch [:set-typeahead-value
                                                     (nth @selections @selected-index)])
                                       (rf/dispatch [:set-typeahead-hidden true]))
                                  ;; TODO: unknown key
                                  27 (do (rf/dispatch [:set-typeahead-hidden true])
                                         (rf/dispatch [:set-typeahead-selected-index 0]))
                                  "default"))}]
       [:ul {:hidden (or (empty? @selections) @typeahead-hidden?)
             :class "typeahead-list"
             :on-mouse-enter (rf/dispatch [:set-mouse-on-list true])
             :on-mouse-leave (rf/dispatch [:set-mouse-on-list false])}
        (doall
         (map-indexed
          (fn [index result]
            [:li {:tab-index index
                  :key index
                  :class (if (= @selected-index index) "highlighted" "typeahead-item")
                  :on-mouse-over #(do
                                    (rf/dispatch [:set-typeahead-selected-index
                                                  (js/parseInt (.getAttribute (.-target %) "tabIndex"))]))
                  :on-click #(do
                               (rf/dispatch [:set-typeahead-hidden true])
                               (rf/dispatch [:set-typeahead-value result]))} result]) @selections))]
       [:pre (str @data-source)]])))

(defn course-row [])

(defn courses-table [])

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
    ;; [search]
    [typeahead]
    ;; [courses-table]
    ;; [my-courses]
    ;; [timetable]
    ]])
