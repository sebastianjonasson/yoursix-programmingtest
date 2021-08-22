(ns yoursix.test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.data.json :as json]
   [clj-http.client :as client]
   [yoursix.main :refer [-main]]))

(deftest tests
  (-main)
  (testing "POST /robots"
    (let [robot {:id "e5deeb54-4b8c-433a-9836-7163fb435bfd"
                 :room {:width 6
                        :depth 8}
                 :initial-position {:x 4
                                    :y 2
                                    :direction "S"}}
          res (client/post "http://localhost:1337/robots" {:throw-exceptions false
                                                           :body (json/write-str robot)
                                                           :content-type :json})
          expected-body {:id "e5deeb54-4b8c-433a-9836-7163fb435bfd"
                         :room {:width 6
                                :depth 8}
                         :initial-position {:x 4
                                            :y 2
                                            :direction "S"}
                         :current-position {:x 4
                                            :y 2
                                            :direction "S"}
                         :moves []}]

      (is (= 200 (:status res)))
      (is (= expected-body (-> res :body (json/read-str :key-fn keyword))))))

  (testing "GET /robots"
    (let [res (client/get "http://localhost:1337/robots" {:throw-exceptions false})
          expected-body [{:id "e5deeb54-4b8c-433a-9836-7163fb435bfd"
                          :room {:width 6
                                 :depth 8}
                          :initial-position {:x 4
                                             :y 2
                                             :direction "S"}
                          :current-position {:x 4
                                             :y 2
                                             :direction "S"}
                          :moves []}]]
      (is (= 200 (:status res)))
      (is (= expected-body (-> res :body (json/read-str :key-fn keyword)))))))
