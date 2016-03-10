(ns rbo.core-test
  (:require [midje.sweet :refer :all]
            [midje.util :refer [expose-testables]]
            [rbo.core :refer :all]))

(expose-testables rbo.core)

(facts
  (fact "rbo"
        (rbo nil nil) => 0.0
        (rbo [] []) => 0.0
        (rbo ["a"] ["a"]) => 1.0
        (rbo ["a"] ["b"]) => 0.0
        (rbo ["a" "b"] ["a" "c"]) => 0.55
        (rbo ["a" "b"] ["a" "c"] 0.9) => 0.55
        (rbo ["a" "b"] ["a" "c"] 0.5) => 0.75
        (rbo ["a" "b"] ["b" "a"]) => 0.9
        (rbo ["a" "b"] ["a" "b"]) => 1.0
        (rbo ["a" "b" "c"] ["a" "b" "c" "d"]) => (roughly 1)
        (rbo [1 2 3] [1 2 3 4]) => (roughly 1)
        (rbo (range 20) (range 30)) => (rbo (range 30) (range 20))
        (rbo (shuffle (range 30)) (shuffle (range 29))) => (do (partial <= 0)
                                                               (partial >= 1)))

  (fact "intersection"
        (intersection nil nil) => {}
        (intersection [] []) => {}
        (intersection ["a"] ["a"]) => {"a" 1}
        (intersection ["a"] ["b"]) => {}
        (intersection ["a" "b"] ["a"]) => {"a" 1}
        (intersection ["b" "c" "a"] ["a" "b"]) => {"b" 1}
        (intersection ["a" "b"] ["b" "c" "a"]) => {"b" 1}
        (intersection ["a" "b"] ["a" "b"] 1) => {"a" 1})

  (fact "overlap"
        (overlap nil nil) => 0
        (overlap [] []) => 0
        (overlap ["a"] ["a"]) => 1
        (overlap ["a"] ["b"]) => 0
        (overlap ["a" "b"] ["a"]) => 1
        (overlap ["b" "c" "a"] ["a" "b"]) => 1
        (overlap ["a" "b"] ["b" "c" "a"]) => 1
        (overlap ["a" "b"] ["a" "b"] 1) => 1)

  (fact "agreement"
        (agreement nil nil) => 0
        (agreement [] []) => 0
        (agreement ["a"] ["a"]) => 1
        (agreement ["a"] ["b"]) => 0
        (agreement ["a" "b"] ["a"]) => 1
        (agreement ["b" "c" "a"] ["a" "b"]) => 1/2
        (agreement ["a" "b"] ["b" "c" "a"]) => 1/2
        (agreement ["a" "b"] ["a" "b"] 1) => 1)

  (fact "min-count"
        (min-count nil nil) => 0
        (min-count [] []) => 0
        (min-count ["a"] []) => 0
        (min-count [] ["a"]) => 0
        (min-count ["a"] ["b"]) => 1
        (min-count ["a" "b"] ["c"]) => 1)

  (fact "merge-common-with"
        (merge-common-with min {} {}) => {}
        (merge-common-with min {:a 1} {}) => {}
        (merge-common-with min {} {:a 1}) => {}
        (merge-common-with min {:a 1} {:b 1}) => {}
        (merge-common-with min {:a 1} {:a 2}) => {:a 1}
        (merge-common-with min {:a 1} {:a 2 :b 1}) => {:a 1}))
