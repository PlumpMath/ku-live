(ns kulive.handlers
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [kulive.db :as db]))


(re-frame/register-handler
 :initialize-db
 (fn [_ _]
   db/default-db))
