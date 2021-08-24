(ns yoursix.robots.core
  (:require [yoursix.data.core :as data-store]
            [yoursix.robots.api.next-position :refer [next-position]]))

(def collection :robots)

(defn- apply-schema [robot]
  (assoc robot
         :current-position (:initial-position robot)
         :moves []))

(defn insert! [robot]
  (data-store/insert! collection (apply-schema robot)))

(defn fetch-all []
  (data-store/fetch collection))

(defn fetch-by-id [id]
  (data-store/fetch-by-id collection id))

(defn update! [id robot]
  (data-store/update! collection id robot))

(defn move! [robot movement]
  (let [position (next-position robot movement)
        moves (-> robot :moves (conj movement))
        next-robot (assoc robot :current-position position
                          :moves moves)]
    (if-not (= (:current-position robot) position)
      (update! (:id robot) next-robot)
      robot)))
