(ns yoursix.router.RequestHandler
  (:require [clojure.data.json :as json]
            [yoursix.router.util.route-matcher :refer [route-matcher]]
            [yoursix.router.util.route-params :refer [route-params]])
  (:import [com.sun.net.httpserver HttpExchange])
  (:gen-class
   :implements [com.sun.net.httpserver.HttpHandler]
   :prefix "-"
   :state state
   :init init
   :main false
   :methods [[setRoutes [java.util.Map] void]]))

(defn -init []
  [[] (atom {})])

(defn set-field
  [this key value]
  (swap! (.state this) into {key value}))

(defn get-field
  [this key]
  (@(.state this) key))

(defn -setRoutes [this body]
  (set-field this :routes body))

(defn send-response! [http-exchange {:keys [status-code body]}]
  (let [headers (.getResponseHeaders http-exchange)
        os (.getResponseBody http-exchange)]

    (.set headers "Content-Type" "application/json")
    (.sendResponseHeaders http-exchange status-code (count body))
    (.write os (.getBytes body))
    (.close os)))

(defn- extract-request-params [http-exchange]
  {:method (.getRequestMethod http-exchange)
   :path (.getPath (.getRequestURI http-exchange))})

(defn- get-request-route [context path]
  (-> (get-field context :routes)
      (route-matcher path)))

(defn- get-request-body [http-exchange]
  (-> http-exchange
      (.getRequestBody)
      (slurp)
      (json/read-str :key-fn keyword
                     :eof-error? false)))

(defn- get-handler [context route method]
  (-> (get-field context :routes)
      (get-in [route method])))

(defn -handle [this ^HttpExchange http-exchange]
  (try
    (let [request-params (extract-request-params http-exchange)
          path (:path request-params)
          route (get-request-route this (:path request-params))
          handler (get-handler this route (:method request-params))]

      (when handler
        (let [params (assoc request-params
                            :route route
                            :params (route-params path route)
                            :body (get-request-body http-exchange))
              response-data (handler params)
              status-code (or (:status-code handler) 200)
              body (or (:body response-data) "")]
          (send-response! http-exchange {:status-code status-code :body body})))

      (when-not handler
        (send-response! http-exchange {:status-code 404 :body "Not found"})))
    (catch Exception e
      (println (str "caught exception: " (.getMessage e))))))
