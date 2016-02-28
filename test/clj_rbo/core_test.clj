(ns clj-rbo.core-test
  (:require [midje.sweet :refer :all]
            [clj-rbo.core :refer :all]))

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
        (rbo ["a" "b" "c"] ["a" "b" "c" "d"]) => (roughly 0.955)
        (rbo [1 2 3] [1 2 3 4]) => (roughly 0.955)
        (rbo (range 20) (range 30)) => (rbo (range 30) (range 20)))

  (fact "intersection"
        (intersection nil nil) => #{}
        (intersection [] []) => #{}
        (intersection ["a"] ["a"]) => #{"a"}
        (intersection ["a"] ["b"]) => #{}
        (intersection ["a" "b"] ["a"]) => #{"a"}
        (intersection ["b" "c" "a"] ["a" "b"]) => #{"b"}
        (intersection ["a" "b"] ["b" "c" "a"]) => #{"b"}
        (intersection ["a" "b"] ["a" "b"] 1) => #{"a"})

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
        (agreement ["a" "b"] ["a" "b"] 1) => 1))
