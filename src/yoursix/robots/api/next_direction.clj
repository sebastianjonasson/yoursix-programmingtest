(ns yoursix.robots.api.next-direction
  (:require [clojure.test :refer [deftest is testing]]))

(def directions ["N" "E" "S" "W"])

(defn next-direction [current movement]
  (let [current-idx (.indexOf directions current)
        idx-op (case movement
                 "L" dec
                 "F" identity
                 "R" inc)]
    (as-> (idx-op current-idx) idx
      (mod idx 4)
      (get directions idx))))

(deftest tests
  (testing "Move Left"
    (is (= "W" (next-direction "N" "L")))
    (is (= "S" (next-direction "W" "L")))
    (is (= "E" (next-direction "S" "L")))
    (is (= "N" (next-direction "E" "L"))))

  (testing "Move Forward"
    (is (= "N" (next-direction "N" "F")))
    (is (= "W" (next-direction "W" "F")))
    (is (= "S" (next-direction "S" "F")))
    (is (= "E" (next-direction "E" "F"))))

  (testing "Move right"
    (is (= "E" (next-direction "N" "R")))
    (is (= "S" (next-direction "E" "R")))
    (is (= "W" (next-direction "S" "R")))
    (is (= "N" (next-direction "W" "R")))))

