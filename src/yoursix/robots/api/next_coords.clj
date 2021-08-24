(ns yoursix.robots.api.next-coords
  (:require [clojure.test :refer [deftest is testing]]))

(defn next-coords [x y direction]
  (case direction
    "N" [x (inc y)]
    "E" [(inc x) y]
    "S" [x (dec y)]
    "W" [(dec x) y]))

(deftest tests
  (testing "Move Left"
    (is (= [3 4] (next-coords 3 3 "N")))
    (is (= [4 3] (next-coords 3 3 "E")))
    (is (= [3 2] (next-coords 3 3 "S")))
    (is (= [2 3] (next-coords 3 3 "W")))))
