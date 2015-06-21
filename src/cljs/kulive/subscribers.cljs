(ns kulive.subscribers
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame])
  (:require-macros [reagent.ratom  :refer [reaction]]))


;; Utils
(defn course-hm->str
  [[_ data]]
  (str/join " "
            [(str/replace (get-in data [:kr :name]) " " "")
             (get-in data [:kr :number])
             (get-in data [:kr :professor])
             (apply str (get-in data [:kr :schedule]))
             (str/replace (get-in data [:kr :credit-hours]) " " "")
             (get-in data [:kr :classification])
             (get-in data [:en :name])
             (get-in data [:en :number])
             (get-in data [:en :professor])
             (apply str (flatten (get-in data [:en :schedule])))
             (str/replace (get-in data [:en :credit-hours]) " " "")
             (get-in data [:en :classification])]))

(defn matches-query?
  [search-input course]
  (let [course-str (str/lower-case (course-hm->str course))
        search-input (str/lower-case search-input)
        search-tokens (str/split search-input #" ")]
    (every? (fn [search-token]
              (> (.indexOf course-str search-token) -1))
            search-tokens)))

;; Subscribers (this will get data based on inner database)

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :search-tokens
 (fn [db]
   (reaction (str/split (str/lower-case (:search-input @db)) #" "))))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

(re-frame/register-sub
 :my-courses
 (fn [db]
   (reaction (:my-courses @db))))

(re-frame/register-sub
 :not-my-courses
 (let [courses (re-frame/subscribe [:courses])
       my-courses (re-frame/subscribe [:my-courses])]
   (fn [db]
     (reaction (filterv (fn [[id _]]
                          (not (get (set @my-courses) id)))
                        @courses)))))

;; TODO: is it right pattern?
(re-frame/register-sub
 :filtered-courses
 (let [search-input (re-frame/subscribe [:search-input])
       not-my-courses (re-frame/subscribe [:not-my-courses])]
   (fn [_]
     (reaction
      (let [filtered-courses
            (filterv (partial matches-query? @search-input) @not-my-courses)]
        (if (and (empty? filtered-courses)
                 (> (count @search-input) 0))
          (filterv
           (partial matches-query? (apply str (butlast @search-input)))
           @not-my-courses)
          filtered-courses))))))

(re-frame/register-sub
 :courses-to-display
 (let [filtered-courses (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (take 5 @filtered-courses)))))

(re-frame/register-sub
 :count-courses-in-search
 (let [filtered (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (count @filtered)))))

(re-frame/register-sub
 :sum-credits-in-my-courses
 (let [my-courses (re-frame/subscribe [:my-courses])
       courses (re-frame/subscribe [:courses])]
   (fn [_]
     (reaction
      (reduce + (mapv #(js/parseInt (first (get-in (val %) [:en :credit-hours])))
                      ;; select @my-courses maps from @courses
                      (select-keys (into {} @courses) @my-courses)))))))

(re-frame/register-sub
 :my-schedule
 (let [my-courses (re-frame/subscribe [:my-courses])
       courses (re-frame/subscribe [:courses])]
   (fn [_]
     (reaction
      (let [courses (into {} @courses)
            result (reduce (fn [acc [day period cid]]
                             (if (get acc [day period])
                               (update-in acc [[day period]] conj cid)
                               (assoc-in acc [[day period]] [cid])))
                           {}
                           (mapcat
                            (fn [cid]
                              (get-in courses [cid :en :schedule]))
                            @my-courses))]
        result)))))
