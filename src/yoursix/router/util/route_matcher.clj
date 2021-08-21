(ns yoursix.router.util.route-matcher
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :refer [starts-with? split]]))

(defn- route-match? [path-parts route-parts]
  (let [parts-range (range 0 (count route-parts))]
    (reduce
     (fn [acc n]
       (let [p (get path-parts n)
             r (get route-parts n)]
         (when acc
           (if (starts-with? r ":")
             true
             (= p r)))))
     parts-range)))

(defn- is-match? [path route]
  (let [path-parts (split path #"/")
        route-parts (split route #"/")]
    (when (and (= (count path-parts) (count route-parts))
               (route-match? path-parts route-parts))
      route)))

(defn route-matcher [routes path]
  (->> routes
       (keys)
       (filter (partial is-match? path))
       (first)))

(deftest tests
  (let [routes {"/api" {"GET" {}}
                "/api/users" {"GET" {}}
                "/api/users/:user-id" {"GET" {}}
                "/api/users/:user-id/stats" {"GET" {}}}]

    (testing "should match /api"
      (let [path "/api"
            expected "/api"
            result (route-matcher routes path)]
        (is (= expected result))))

    (testing "should match /api/users"
      (let [path "/api/users"
            expected "/api/users"
            result (route-matcher routes path)]
        (is (= expected result))))

    (testing "should match /api/users/user1234"
      (let [path "/api/users/user1234"
            expected "/api/users/:user-id"
            result (route-matcher routes path)]
        (is (= expected result))))

    (testing "/api/test should not match /users/test"
      (let [path "/api/test"
            result (route-matcher {"/users/test" {"GET" {}}} path)]
        (is (not result))))

    (testing "/api should not match /test"
      (let [path "/api/test"
            result (route-matcher {"/test/test" {"GET" {}}} path)]
        (is (not result))))

    (testing "/api should not match /api/users"
      (let [path "/api"
            result (route-matcher {"/api/users" {"GET" {}}} path)]
        (is (not result))))))
