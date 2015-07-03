(ns kulive.handlers
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [kulive.db :as db]))

(re-frame/register-handler
 :initialize-db
 (fn [_ _]
   db/default-db))

(defn hide-typeahead
  [db]
  (assoc-in db [:typeahead :hidden?] true))

(re-frame/register-handler :hide-typeahead hide-typeahead)

(defn show-typeahead
  [db]
  (assoc-in db [:typeahead :hidden?] false))

(re-frame/register-handler :show-typeahead show-typeahead)

(defn set-typeahead-selected-index
  [db [_ ind]]
  (assoc-in db [:typeahead :selected-index] ind))

(re-frame/register-handler :set-typeahead-selected-index set-typeahead-selected-index)

(defn set-typeahead-selections
  [db [_ data-source]]
  (assoc-in db [:typeahead :selections] data-source))

(re-frame/register-handler :set-typeahead-selections set-typeahead-selections)

(defn set-typeahead-value
  [db [_ val]]
  (assoc-in db [:typeahead :value] val))

(re-frame/register-handler :set-typeahead-value set-typeahead-value)

(defn set-mouse-on-list
  [db [_ bool]]
  (assoc-in db [:typeahead :mouse-on-list?] bool))

(re-frame/register-handler :set-mouse-on-list set-mouse-on-list)
