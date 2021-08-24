(ns yoursix.router.core
  (:require [yoursix.router.util.request-handler :refer [request-handler]])
  (:import [java.net InetSocketAddress]
           [com.sun.net.httpserver HttpServer]))

(defn GET [url handler]
  {:method "GET"
   :path url
   :handler handler})

(defn POST [url handler]
  {:method "POST"
   :path url
   :handler handler})

(defn- start [http-server]
  (.start http-server))

(defn- stop [http-server]
  (.stop http-server 0))

(defn init [routes port root-url]
  (let [socket-address (InetSocketAddress. port)
        http-server (HttpServer/create socket-address 0)
        start-server (partial start http-server)
        stop-server (partial stop http-server)
        request-handler (request-handler routes)]
    (.createContext http-server root-url request-handler)
    [start-server
     stop-server]))
