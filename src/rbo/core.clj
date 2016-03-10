(ns rbo.core
  (:require [clojure.math.numeric-tower :refer [expt]]
            [clojure.set :as set]))

(defn- ^{:testable true} min-count
  "Returns the minimum count of args."
  [& args]
  (apply min (map count args)))

(defn- ^{:testable true} merge-common-with
  "Returns a map that consists of the common keys of m1 and m2.
  The values will be combined by calling (f val-in-smaller-or-m2 val-in-larger-or-m1)."
  [f m1 m2]
  (let [[a b] (if (< (count m1) (count m2))
                [m1 m2]
                [m2 m1])]
    (persistent!
      (reduce-kv (fn [out k v]
                   (if (contains? b k)
                     (assoc! out k (f (get a k) (get b k)))
                     out))
                 (transient {})
                 a))))

(defn intersection
  "Return the multiset intersection, as a frequency map whose counts are the element-wise minima
  of counts in r1 and r2, to the length of the shorter input or a provided depth."
  ([r1 r2] (intersection r1 r2 (min-count r1 r2)))
  ([r1 r2 d] (merge-common-with min (frequencies (take d r1)) (frequencies (take d r2)))))

(defn overlap
  "Return the size of the intersection of the inputs to the length of the shorter input or a provided depth."
  ([r1 r2] (overlap r1 r2 (min-count r1 r2)))
  ([r1 r2 d] (reduce + (vals (intersection r1 r2 d)))))

(defn agreement
  "Return the proportion of overlap of the inputs to the length of the shorter input or a provided depth."
  ([r1 r2] (agreement r1 r2 (min-count r1 r2)))
  ([r1 r2 d] (if (zero? d) 0 (/ (overlap r1 r2 d) d))))

(defmulti ^:private rbo-mm
          (fn [r1 r2 _]
            (if (= (count r1) (count r2)) :even :uneven)))

(defmethod rbo-mm :even
  [r1 r2 p]
  (let [k (count r1)
        k-seq (range 1 (+ 1 k))
        pow (partial expt p)
        a (partial agreement r1 r2)]
    (+ (* (a k) (pow k)) (* (/ (- 1 p) p) (reduce + (map #(* (a %) (pow %)) k-seq))))))

(defmethod rbo-mm :uneven
  [r1 r2 p]
  (let [r1-count (count r1)
        r2-count (count r2)
        [l l-count s s-count] (if (> r1-count r2-count) [r1 r1-count r2 r2-count] [r2 r2-count r1 r1-count])
        l-seq (range 1 (+ 1 l-count))
        l-tail (take-last (- l-count s-count) l-seq)
        pow (partial expt p)
        a (partial agreement l s)
        o (partial overlap l s)
        a-s (a s-count)]
    (+ (* (/ (- 1 p) p)
          (+ (reduce + (map #(* (a %) (pow %)) l-seq))
             (reduce + (map #(* a-s (/ (- % s-count) %) (pow %)) l-tail))))
       (* (+ a-s (/ (- (o l-count) (o s-count)) l-count)) (pow l-count)))))

(defn rbo
  "Return the extrapolated rank-biased overlap of the inputs using a persistence of p (default 0.9)."
  ([r1 r2] (rbo r1 r2 0.9))
  ([r1 r2 p] (rbo-mm r1 r2 p)))
