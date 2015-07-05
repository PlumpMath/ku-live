(ns kulive.utils)

(defn value-of [element]
  (-> element .-target .-value))
