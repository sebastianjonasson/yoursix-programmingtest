(ns yoursix.robots.core
  (:require [yoursix.data.core :as data-store]))

(def collection :robots)

(defn- apply-schema [robot]
  (assoc robot
         :current-position (:initial-position robot)
         :moves []))

(defn insert! [robot]
  (data-store/insert! collection (apply-schema robot)))

(defn fetch-all []
  (data-store/fetch collection))

