(ns kulive.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

(rf/register-sub
 :typeahead-db
 (fn [db]
   (reaction (:typeahead @db))))
