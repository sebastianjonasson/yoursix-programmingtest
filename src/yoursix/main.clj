(ns yoursix.main
  (:require [yoursix.api.core :as api]
            [clojure.tools.logging :as log]))

(def PORT (or (System/getenv "PORT") 1337))

(defn -main [& args]
  (log/info "YourSix API running")
  (log/info "Access at http://localhost:" PORT)
  (api/start PORT))

