(ns yoursix.test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.string :refer [split]]
   [clojure.data.json :as json]
   [clj-http.client :as client]
   [yoursix.main :refer [-main]]))

(defn- move-request [robot-id move]
  (client/post
   (str "http://localhost:1337/robots/" robot-id "/move")
   {:throw-exceptions false
    :body (json/write-str move)
    :content-type :json}))

(defn- apply-moves [robot-id movements]
  (doseq [move (split movements #"")]
    (let [res (move-request robot-id move)]
      (is (= 200 (:status res))))))

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
      (is (= expected-body (-> res :body (json/read-str :key-fn keyword))))))

  (testing "POST /robots/:robot-id/move"
    (let [robot-id "020303d3-a7fb-437e-af49-91d35821b3da"
          robot {:id robot-id
                 :room {:width 5
                        :depth 5}
                 :initial-position {:x 0
                                    :y 0
                                    :direction "N"}}
          res (client/post "http://localhost:1337/robots" {:throw-exceptions false
                                                           :body (json/write-str robot)
                                                           :content-type :json})
          _ (is (= 200 (:status res)))
          move-res-1 (move-request robot-id "F")
          move-body-1 (-> move-res-1 :body (json/read-str :key-fn keyword))
          move-res-2 (move-request robot-id "R")
          move-body-2 (-> move-res-2 :body (json/read-str :key-fn keyword))]
      (is (= 200 (:status move-res-1)))
      (is (= 200 (:status move-res-2)))

      (is (= 0 (-> move-body-1 :current-position :x)))
      (is (= 1 (-> move-body-1 :current-position :y)))
      (is (= "N" (-> move-body-1 :current-position :direction)))
      (is (= ["F"] (-> move-body-1 :moves)))


      (is (= 1 (-> move-body-2 :current-position :x)))
      (is (= 1 (-> move-body-2 :current-position :y)))
      (is (= "E" (-> move-body-2 :current-position :direction)))
      (is (= ["F" "R"] (-> move-body-2 :moves)))))

  (testing "Yoursix testcase RFRFFRFRF"
    (let [movements "RFRFFRFRF"
          robot-id "99154b4e-006f-4509-81f6-77f49ff43db8"
          robot {:id robot-id
                 :room {:width 5
                        :depth 5}
                 :initial-position {:x 1
                                    :y 3
                                    :direction "N"}}
          res (client/post "http://localhost:1337/robots" {:throw-exceptions false
                                                           :body (json/write-str robot)
                                                           :content-type :json})
          _ (is (= 200 (:status res)))
          _ (apply-moves robot-id movements)
          robot-status-res (client/get (str "http://localhost:1337/robots/" robot-id))
          body (-> robot-status-res :body (json/read-str :key-fn keyword))]
      (is (= 200 (:status robot-status-res)))
      (is (= 1 (-> body :current-position :x)))
      (is (= 2 (-> body :current-position :y)))
      (is (= "N" (-> body :current-position :direction)))
      (is (= (split movements #"") (-> body :moves))))))

