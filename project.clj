(defproject kulive "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0-alpha6"]
                 [ring-server "0.4.0"]
                 [cljsjs/react "0.13.3-0"]
                 [reagent "0.5.0"]
                 [reagent-forms "0.5.1"]
                 [reagent-utils "0.1.4"]
                 [org.clojure/clojurescript "0.0-3308" :scope "provided"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.5"]
                 [prone "0.8.2"]
                 [compojure "1.3.4"]
                 [selmer "0.8.2"]
                 [environ "1.0.0"]
                 [secretary "1.2.3"]
                 [re-frame "0.4.1"]
                 [expectations "2.1.1"]
                 [cljs-ajax "0.3.13"]
                 [fogus/ring-edn "0.3.0"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.6"]
            [lein-asset-minifier "0.2.2"]
            [lein-expectations "0.0.7"]
            [cider/cider-nrepl "0.9.0-SNAPSHOT"]
            [lein-autoexpect "1.4.0"]]

  :source-paths ["src/clj"]

  :min-lein-version "2.5.0"

  :uberjar-name "kulive.jar"

  :main kulive.server

  :clean-targets ^{:protect false} ["resources/public/js"]

  :test-paths ["test/clj"]

  ;; TODO: later before production we should minify averything in one big css
  :minify-assets
  {:assets
   {"resources/public/css/skeleton.min.css" "resources/public/css/skeleton.css"
    "resources/public/css/normalize.min.css" "resources/public/css/normalize.css"
    "resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to "resources/public/js/app.js"
                                        :output-dir "resources/public/js/out"
                                        :asset-path "js/out"
                                        :optimizations :none
                                        :pretty-print true}}}}

  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [leiningen "2.5.1"]
                        [figwheel "0.3.3"]
                        [weasel "0.7.0"]
                        [com.cemerick/piggieback "0.2.1"]
                        [pjstadig/humane-test-output "0.7.0"]]

         :source-paths ["env/dev/clj"]
         :plugins [[lein-figwheel "0.3.3" :exclusions [cider/cider-nrepl]]]

         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]

         :figwheel {:http-server-root "public"
                    :server-port 3449
                    ;; to use cljs-repl, cider-connect and:
                    ;; user> (use 'figwheel-sidecar.repl-api)
                    ;; user> (cljs-repl)
                    :nrepl-port 7002
                    :css-dirs ["resources/public/css"]
                    :ring-handler kulive.handler/app}

         :env {:dev? true}

         :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                    :compiler {:main "kulive.dev"
                                               :source-map true}}}}}

   :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
             :env {:production true}
             :aot :all
             :omit-source true
             :cljsbuild {:jar true
                         :builds {:app
                                  {:source-paths ["env/prod/cljs"]
                                   :compiler
                                   {:optimizations :advanced
                                    :pretty-print false}}}}}})
