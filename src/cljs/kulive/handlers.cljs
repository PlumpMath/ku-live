(ns kulive.handlers
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [kulive.db :as db]))

(rf/register-handler
 :initialize-db
 (fn [_ _]
   db/default-db))

(defn set-typeahead-hidden
  [db [_ bool]]
  (assoc-in db [:typeahead :hidden?] bool))

(rf/register-handler :set-typeahead-hidden set-typeahead-hidden)

(defn set-typeahead-selected-index
  [db [_ ind]]
  (assoc-in db [:typeahead :selected-index] ind))

(rf/register-handler :set-typeahead-selected-index set-typeahead-selected-index)

(defn set-typeahead-selections
  [db [_ selections]]
  (assoc-in db [:typeahead :selections] selections))

(rf/register-handler :set-typeahead-selections set-typeahead-selections)

(defn set-typeahead-value
  [db [_ val]]
  (assoc-in db [:typeahead :value] val))

(rf/register-handler :set-typeahead-value set-typeahead-value)

(defn set-mouse-on-list
  [db [_ bool]]
  (assoc-in db [:typeahead :mouse-on-list?] bool))

(rf/register-handler :set-mouse-on-list set-mouse-on-list)
