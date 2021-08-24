(ns yoursix.robots.api.coords-inbound
  (:require [clojure.test :refer [deftest is testing]]))

(defn coords-inbound? [robot x y]
  (let [width (-> robot :room :width)
        depth (-> robot :room :depth)]
    (and (< -1 x width)
         (< -1 y depth))))

(deftest tests
  (let [robot {:room {:width 3 :depth 3}}]
    (testing "Inbound coords should return true"
      (is (coords-inbound? robot 0 0))
      (is (coords-inbound? robot 1 1))
      (is (coords-inbound? robot 0 2))
      (is (coords-inbound? robot 2 2)))
    (testing "Out of bound coords should return false"
      (is (false? (coords-inbound? robot -1 -1)))
      (is (false? (coords-inbound? robot -1 0)))
      (is (false? (coords-inbound? robot 0 -1)))
      (is (false? (coords-inbound? robot 3 3)))
      (is (false? (coords-inbound? robot 3 0)))
      (is (false? (coords-inbound? robot 0 3))))))

