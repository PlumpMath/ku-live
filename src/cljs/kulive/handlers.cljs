(ns kulive.handlers
  (:require [kulive.db :as db]
            [re-frame.core :as rf]))

(rf/register-handler
 :initialize-db
 (fn [_ _]
   db/default-db))

(defn set-typeahead-hidden
  [db [_ bool]]
  (assoc-in db [:typeahead :hidden?] bool))

(rf/register-handler :set-typeahead-hidden set-typeahead-hidden)

(defn set-typeahead-index
  [db [_ ind]]
  (assoc-in db [:typeahead :index] ind))

(rf/register-handler :set-typeahead-index set-typeahead-index)

(defn select-typeahead
  [db [_ selections]]
  (assoc-in db [:typeahead :selections] selections))

(rf/register-handler :select-typeahead select-typeahead)

(defn set-typeahead-val
  [db [_ val]]
  (assoc-in db [:typeahead :value] val))

(rf/register-handler :set-typeahead-val set-typeahead-val)

(defn set-mouse-on-list
  [db [_ bool]]
  (assoc-in db [:typeahead :mouse-on-list?] bool))

(rf/register-handler :set-mouse-on-list set-mouse-on-list)
