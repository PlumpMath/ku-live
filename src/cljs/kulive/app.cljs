(ns kulive.app
  (:require [reagent.core :as reagent]
            [kulive.views :refer [home-page]]))

(defn init []
  (reagent/render-component [home-page]
                            (.getElementById js/document "container")))
