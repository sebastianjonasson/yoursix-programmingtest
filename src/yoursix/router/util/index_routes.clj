(ns yoursix.router.util.index-routes
  (:require [clojure.test :refer [deftest is testing]]))

(defn index-routes [routes]
  (reduce
   (fn [acc {:keys [method path handler]}]
     (let [path-group (or (get acc path) {})
           next-group (assoc path-group method handler)]
       (assoc acc path next-group)))
   {}
   routes))

(deftest tests
  (testing "should index routes {:path {:method handler}}"
    (let [routes [{:method "GET"
                   :path "/test/path"
                   :handler string?}
                  {:method "POST"
                   :path "/test/path"
                   :handler keyword?}
                  {:method "POST"
                   :path "/api/users"
                   :handler nil?}]
          expected {"/test/path" {"GET" string?
                                  "POST" keyword?}
                    "/api/users" {"POST" nil?}}
          actual (index-routes routes)]
      (is (= expected actual)))))
