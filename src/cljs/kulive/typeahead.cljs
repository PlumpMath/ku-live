(ns kulive.typeahead
  (:require [re-frame.core :as rf]
            [kulive.utils :refer [value-of]]
            [kulive.subs]
            [kulive.handlers])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn typeahead []
  (let [typeahead-db (rf/subscribe [:typeahead-db])
        data-source (fn [text]
                      (filter
                       #(-> (.toLowerCase %) (.indexOf text) (> -1))
                       ["Alice" "Alan" "Bob" "Beth" "Jim" "Jane" "Kim" "Rob" "Zoe"]))
        value (reaction (:value @typeahead-db))
        selected-index (reaction (:selected-index @typeahead-db))
        selections (reaction (:selections @typeahead-db))
        typeahead-hidden? (reaction (:hidden? @typeahead-db))
        mouse-on-list? (reaction (:mouse-on-list? @typeahead-db))]
    (fn []
      [:div
       [:input
        {:type "text"
         :style {:width "70%"}
         :value @value
         :on-blur #(when-not @mouse-on-list?
                     (rf/dispatch [:set-typeahead-hidden true])
                     (rf/dispatch [:set-typeahead-selected-index 0]))
         :on-change #(do
                       (rf/dispatch [:set-typeahead-selections (data-source (.toLowerCase (value-of %)))])
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
                           ;; Esc key
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
       [:div                            ; Just for visualizaiton
        [:p (str "data source: " (data-source @value))]
        [:p (str "value: " @value)]
        [:p (str "typeahead-hidden: " @typeahead-hidden?)]
        [:p (str "mouse on list: " @mouse-on-list?)]
        [:p (str "selections: " @selections)]
        [:p (str "selected index: " @selected-index)]]])))
