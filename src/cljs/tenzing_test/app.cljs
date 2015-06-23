(ns tenzing-test.app
  (:require [reagent.core :as reagent :refer [atom]]))

(defn some-component []
  [:div
   [:h3 "My name is Sooheon"]
   [:p.someclass
    "Hello julia"]])

(defn calling-component []
  [:div
   [:h1 "Hello world"]
   [some-component]])

(defn init []
  (reagent/render-component [calling-component]
                            (.getElementById js/document "container")))
