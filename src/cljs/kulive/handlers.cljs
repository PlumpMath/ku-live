(ns kulive.handlers
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [ajax.core :refer [GET POST]]))


;; Handlers (this will modify inner database)

(re-frame/register-handler
 :init-courses
 (fn [app-state [_ courses]]
   (assoc-in app-state [:courses] courses)))

(defn handler [response]
  (re-frame/dispatch [:init-courses response]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(re-frame/register-handler
 :initialise-db
 (fn [_ _]
   (GET "/get-db" {:handler handler :error-handler error-handler})
   {:courses {}
    :search-input ""
    :my-courses []}))

(re-frame/register-handler
 :search-input-entered
 (fn [app-state [_ search-input]]
   (assoc-in app-state [:search-input] search-input)))

(defn handle-course-added
  [app-state _]
  (let [displayed-courses (re-frame/subscribe [:courses-to-display])
        search-elem (.getElementById js/document "search-courses")]
    (if (and (= 1 (count @displayed-courses))
             (not (get (set (get app-state :my-courses))
                       (first @displayed-courses))))
      (do (set! (.-value search-elem) "")
          (-> app-state
              (update-in [:my-courses]
                         conj
                         (ffirst @displayed-courses)) ; course-id
              (assoc-in [:search-input] "")))
      app-state)))

(re-frame/register-handler
 :course-added
 handle-course-added)

(defn handle-course-dropped
  [app-state [_ course]]
  (update-in app-state [:my-courses]
             (partial remove #(= % course))))

(re-frame/register-handler
 :course-dropped
 handle-course-dropped)

(defn handle-drop-all-courses
  [app-state _]
  (assoc-in app-state [:my-courses] []))

(re-frame/register-handler
 :drop-all-courses
 handle-drop-all-courses)
