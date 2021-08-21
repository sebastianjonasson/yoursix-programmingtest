(ns yoursix.router.test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [spy.core :as spy]
            [yoursix.router.core :refer [init GET POST]])
  (:import [java.net ConnectException]))

(deftest actions
  (testing "Router actions"
    (let [[start stop] (init [] 1337 "/")]

      (testing "Should be able to start"
        (start)
        (let [res (client/get "http://localhost:1337/test" {:throw-exceptions false})]
          (is (= 404 (:status res)))))

      (testing "Should be able to stop"
        (stop)
        (is
         (thrown-with-msg?
          ConnectException
          #"Connection refused"
          (client/get "http://localhost:1337/test")))))))

(deftest paths
  (testing "Router paths"

    (testing "handler GET /test"
      (let [route (GET "/test" (constantly "hello"))
            [start stop] (init [route] 1337 "/")]
        (start)

        (testing "GET /test request should respond with 200"
          (let [res (client/get "http://localhost:1337/test" {:throw-exceptions false})]
            (is (= 200 (:status res)))))

        (testing "POST /test request should respond with 404"
          (let [res (client/post "http://localhost:1337/test" {:throw-exceptions false})]
            (is (= 404 (:status res)))))
        (stop)))

    (testing "handler GET /test/:id"
      (let [route (GET "/test/:id" (constantly "hello"))
            [start stop] (init [route] 1337 "/")]
        (start)

        (testing "GET /test/someid request should respond with 200"
          (let [res (client/get "http://localhost:1337/test/someid" {:throw-exceptions false})]
            (is (= 200 (:status res)))))
        (stop)))

    (testing "handler should receive request params"
      (let [handler (spy/stub (constantly nil))
            route (GET "/test/:id" handler)
            [start stop] (init [route] 1337 "/")]
        (start)

        (testing "request params should include method, path, route, and params"
          (let [res (client/get "http://localhost:1337/test/1234" {:throw-exceptions false})]
            (is (= 200 (:status res)))
            (is (spy/called-with? handler {:method "GET"
                                           :path "/test/1234"
                                           :route "/test/:id"
                                           :body nil
                                           :params {:id "1234"}}))))
        (stop)))

    (testing "handler POST /test"
      (let [route (POST "/test" (constantly "hello"))
            [start stop] (init [route] 1337 "/")]
        (start)
        (testing "POST /test request should respond with 200"
          (let [res (client/post "http://localhost:1337/test" {:throw-exceptions false} nil)]
            (is (= 200 (:status res)))))
        (stop)))))

(deftest handlers
  (testing "handler should receive request params"
    (let [handler (spy/stub (constantly nil))
          route (GET "/test/:id" handler)
          [start stop] (init [route] 1337 "/")]
      (start)

      (testing "request params should include method, path, route, and params"
        (let [res (client/get "http://localhost:1337/test/1234" {:throw-exceptions false})]
          (is (= 200 (:status res)))
          (is (spy/called-with? handler {:method "GET"
                                         :path "/test/1234"
                                         :route "/test/:id"
                                         :body nil
                                         :params {:id "1234"}}))))
      (stop)))
  (testing "handler should receive request payload"
    (let [handler (spy/stub (constantly nil))
          route (POST "/test" handler)
          [start stop] (init [route] 1337 "/")]
      (start)

      (testing "request params should include request body"
        (let [res (client/post "http://localhost:1337/test" {:throw-exceptions false
                                                             :body (json/write-str {:foo "bar"})
                                                             :content-type :json})]
          (is (= 200 (:status res)))
          (is (spy/called-with? handler {:method "POST"
                                         :path "/test"
                                         :route "/test"
                                         :params {}
                                         :body {:foo "bar"}}))))
      (stop))))


