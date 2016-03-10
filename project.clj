(defproject rbo "0.1.1"
  :description "An implementation of Rank-Biased Overlap in Clojure"
  :url "https://github.com/neverfox/clj-rbo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :author "Roman Pearah"
            :year 2016}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.2"]]}})
