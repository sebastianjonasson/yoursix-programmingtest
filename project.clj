(defproject yoursix "0.1.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/data.json "2.4.0"]]
  :profiles {:dev {:dependencies [[lambdaisland/kaocha "1.0.632"]
                                  [org.clojure/tools.logging "1.1.0"]
                                  [tortue/spy "2.9.0"]
                                  [clj-http "3.12.3"]
                                  ]}}
  :aot [yoursix.router.RequestHandler]
  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]}
  :kaocha {:dependencies [[lambdaisland/kaocha "1.0.632"]]
           :jvm-opts ["-Djdk.tls.client.protocols=TLSv1,TLSv1.1,TLSv1.2"]}
  :main yoursix.main
  :test-paths ["src"])

