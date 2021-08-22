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

(defn- handle-create-robot [params]
  (-> params
      :body
      robots/insert!
      json-response))

(defn start []
  (let [index-robots (router/GET "/robots" handle-index-robots)
        post-robot (router/POST "/robots" handle-create-robot)
        [start _] (router/init [index-robots post-robot] 1337 "/")]
    (start)))

