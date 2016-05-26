(defproject k-means "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [incanter/incanter "1.9.0"]
                 [org.clojure/math.combinatorics "0.1.1"]
                 [incanter-gorilla "0.1.0"]]
  :main ^:skip-aot k-means.core
  :target-path "target/%s"
  :plugins [[lein-gorilla "0.3.4"]
            [lein-ancient "0.6.8"]]
  :profiles {:uberjar {:aot :all}})
