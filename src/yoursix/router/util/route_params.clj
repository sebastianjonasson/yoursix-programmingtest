(ns yoursix.router.util.route-params
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :refer [starts-with? split replace]]))

(defn- ->keyword [part]
  (-> part
      (replace #":" "")
      (keyword)))

(defn route-params [path route]
  (let [path-parts (split path #"/")
        route-parts (split route #"/")
        parts-range (->> route-parts
                         (count)
                         (range 0))]

    (reduce
     (fn [acc n]
       (let [p (get path-parts n)
             r (get route-parts n)]
         (if (starts-with? r ":")
           (assoc acc (->keyword r) p)
           acc)))
     {}
     parts-range)))

(deftest tests
  (testing "Should extract route params"
    (testing "/api/users/stats"
      (let [path "/api/users/stats"
            route "/api/users/stats"
            expected {}
            actual (route-params path route)]
        (is (= expected actual))))

    (testing "/users/:user-id/stats/:stat-id"
      (let [path "/users/user-id-1/stats/stat-id-1"
            route "/users/:user-id/stats/:stat-id"
            expected {:user-id "user-id-1"
                      :stat-id "stat-id-1"}
            actual (route-params path route)]
        (is (= expected actual))))))

