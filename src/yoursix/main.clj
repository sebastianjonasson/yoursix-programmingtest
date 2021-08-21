(ns yoursix.main
  (:require [yoursix.router.core :as router]
            [yoursix.api.core :as api]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]
            [clojure.test :refer [deftest is testing]]))

(defn -main [& args]
  (log/info "started at some port"); TODO from env arg?
  (log/spy "WAT")
  (api/start))

