(defproject nubank/lein-jupyter "0.2.24-NUBANK-4"
  :description "Leiningen plugin for jupyter notebook."
  :url "https://github.com/nubank/lein-jupyter"
  :license {:name "MIT License"}

  :repositories [["publish" {:url "https://clojars.org/repo"
                             :username :env/clojars_username
                             :password :env/clojars_passwd
                             :sign-releases false}]]

  :dependencies [[clojupyter "0.3.6"]                           ;; this dependency needs to be
                                                                ;; updated in leiningen.jupyter.kernel
                                                                ;; manually.
                 [org.apache.commons/commons-exec "1.3"]]

  :profiles {:dev {:plugins [[lein-project-version "0.1.0"]]}}

  :resource-paths ["resources"]
  :eval-in-leiningen true)
