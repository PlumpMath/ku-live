(ns kulive.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [selmer.parser :refer [render-file]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [kulive.parsing :refer [parsed-db]]))


(defn get-db []
  {:status 200
   :headers {"Content-Type" "application/edn; charset=utf-8"}
   :body (pr-str @parsed-db)})

(defroutes routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (GET "/get-db" [] (get-db))
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-edn-params (wrap-defaults routes site-defaults))]
    (if (env :dev?) (wrap-exceptions handler) handler)))
