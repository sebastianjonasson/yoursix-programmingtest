(ns yoursix.router.util.request-handler
  (:require [yoursix.router.util.index-routes :refer [index-routes]])
  (:import [yoursix.router RequestHandler]))

(defn request-handler [routes]
  (let [instance (RequestHandler.)]
    (.setRoutes instance (index-routes routes))
    instance))
