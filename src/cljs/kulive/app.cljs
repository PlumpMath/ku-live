(ns kulive.app
  (:require [kulive.handlers]
            [kulive.views :refer [home-page]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (r/render-component [home-page]
                            (.getElementById js/document "app")))
