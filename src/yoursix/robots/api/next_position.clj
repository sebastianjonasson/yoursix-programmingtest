(ns yoursix.robots.api.next-position
  (:require [clojure.test :refer [deftest is testing]]
            [yoursix.robots.api.next-direction :refer [next-direction]]
            [yoursix.robots.api.next-coords :refer [next-coords]]
            [yoursix.robots.api.coords-inbound :refer [coords-inbound?]]
            [clojure.string :refer [split]]))

(defn next-position [robot movement]
  (let [direction (-> robot :current-position :direction)
        x (-> robot :current-position :x)
        y (-> robot :current-position :y)
        next-direction (next-direction direction movement)
        [next-x next-y] (next-coords x y next-direction)]

    (if (coords-inbound? robot x y)
      {:x next-x
       :y next-y
       :direction next-direction}
      (:current-position robot))))


(defn- apply-moves [robot movements]
  (loop [moves (split movements #"")
         position (:current-position robot)]
    (let [move (first moves)
          remainder (rest moves)
          next (next-position (assoc robot :current-position position) move)]
      (if (-> remainder count pos?)
        (recur remainder next)
        next))))

(deftest tests
  (testing "test case #1"
    (let [movements "FFFF"
          robot {:room {:width 5
                        :depth 5}
                 :current-position {:x 4
                                    :y 4
                                    :direction "S"}}
          expected {:x 4 :y 0 :direction "S"}
          actual (apply-moves robot movements)]
      (is (= expected actual))))

  (testing "test case #2"
    (let [movements "FFRRLL"
          robot {:room {:width 5
                        :depth 5}
                 :current-position {:x 4
                                    :y 4
                                    :direction "S"}}
          expected {:x 2 :y 2 :direction "S"}
          actual (apply-moves robot movements)]
      (is (= expected actual))))

  (testing "Yoursix testcase #1 (modified)"
    (let [movements "RFRFFRFRF"
          robot {:room {:width 5
                        :depth 5}
                 :current-position {:x 1
                                    :y 3
                                    :direction "N"}}
          expected {:x 1 :y 2 :direction "N"}
          actual (apply-moves robot movements)]
      (is (= expected actual))))

  (testing "Yoursix testcase #2 (modified)"
    (let [movements "RFLFFLRF"
          robot {:room {:width 6
                        :depth 6}
                 :current-position {:x 0
                                    :y 2
                                    :direction "E"}}
          expected {:x 5 :y 1 :direction "E"}
          actual (apply-moves robot movements)]
      (is (= expected actual)))))
