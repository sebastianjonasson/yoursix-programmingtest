(ns yoursix.api.core
  (:require [yoursix.router.core :as router]
            [clojure.data.json :as json]
            [yoursix.robots.core :as robots]))

(defn json-response [body]
  (->> body
       (json/write-str)
       (assoc {} :body)))

(defn- handle-index-robots [_]
  (-> (robots/fetch-all)
      (json-response)))

(defn- handle-robot-by-id [request]
  (-> (robots/fetch-by-id (-> request :params :robot-id))
      (json-response)))

(defn- handle-create-robot [request]
  (-> request
      :body
      robots/insert!
      json-response))

(defn- handle-move-robot [request]
  (let [robot-id (get-in request [:params :robot-id])
        movement (:body request)
        robot (robots/fetch-by-id robot-id)]

    (when (and robot (contains? #{"L" "F" "R"} movement))
      (-> (robots/move! robot movement)
          (json-response)))))

(defn start []
  (let [routes [(router/GET "/robots" handle-index-robots)
                (router/POST "/robots" handle-create-robot)
                (router/GET "/robots/:robot-id" handle-robot-by-id)
                (router/POST "/robots/:robot-id/move" handle-move-robot)]
        [start] (router/init routes 1337 "/")]
    (start)))

