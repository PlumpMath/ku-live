(ns kulive.app
  (:require [reagent.core :as reagent]
            [kulive.views :refer [home-page]]
            [kulive.handlers]
            [re-frame.core :as re-frame]))

(defn init []
  (re-frame/dispatch-sync [:initialize-db])
  (reagent/render-component [home-page]
                            (.getElementById js/document "app")))
