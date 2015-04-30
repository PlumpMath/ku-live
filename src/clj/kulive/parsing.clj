(ns kulive.parsing
  (:require [clojure.edn :as edn]
            [clojure.string :as str]))

(def db2015-1R
  (delay (edn/read-string
          (slurp "resources/db/db2015-1R.edn"))))

(defn parse-one-schedule [s]
  (let [s (str/trim s)
        parsed (re-matches #"([A-Z][a-z]{2})\((\d+)(-\d+)?\)(.*)" s)
        day (get parsed 1)
        start-time (Integer/parseInt (get parsed 2))
        end-time (if (get parsed 3)
                   (Integer/parseInt (str/join "" (drop 1 (get parsed 3))))
                   start-time)
        auditory (str/trim (get parsed 4))]
    (mapv (fn [t] [day t auditory])
          (range start-time (inc end-time)))))

(defn parse-schedule [sx]
  (vec (mapcat parse-one-schedule sx)))

(defn process-db [db]
  (into {}
        (map (fn [class]
               (-> class
                   (update-in [1 :en :schedule] parse-schedule)
                   (update-in [1 :kr] dissoc :schedule)))) ;; dont need doublicate of information in DB
        db))

(def parsed-db (delay (take 100 (process-db @db2015-1R))))

#_(into {} (take 10 (process-db @db2015-1R)))
#_(take 10 @db2015-1R)
