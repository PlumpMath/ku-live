(ns kulive.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))
