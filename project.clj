(defproject nubank/lein-jupyter "0.2.24-NUBANK-4"
  :description "Leiningen plugin for jupyter notebook."
  :url "https://github.com/nubank/lein-jupyter"
  :license {:name "MIT License"}

  :repositories [["central" {:url "https://repo1.maven.org/maven2/" :snapshots false}]
                 ["clojars" {:url "https://clojars.org/repo/"}]]

  :dependencies [[clojupyter "0.3.6"]                           ;; this dependency needs to be
                                                                ;; updated in leiningen.jupyter.kernel
                                                                ;; manually.
                 [org.apache.commons/commons-exec "1.3"]]
  :resource-paths ["resources"]
  :eval-in-leiningen true)
