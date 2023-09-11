(ns test-leiningen.util-test
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.test :refer [deftest is]]
   [leiningen.jupyter.utils :as utils]))

(defn mock-sh
  [output]
  (fn [& _args] output))

(def version-output
  "Selected Jupyter core packages...\nIPython          : 8.12.2\nipykernel        : 6.25.0\njupyterlab       : 3.6.3\nnbclient         : 0.5.13\nnotebook         : 6.5.2")

(deftest jupyter-versions-test
  (with-redefs [sh (mock-sh {:exit 0 :out version-output})]
    (is (= {"IPython"    "8.12.2"
            "ipykernel"  "6.25.0"
            "jupyterlab" "3.6.3"
            "nbclient"   "0.5.13"
            "notebook"   "6.5.2"}
           (utils/jupyter-versions "jupyter-test")))))

(deftest major-version-test
  (is (= 3
         (utils/major-version "3.6.2"))))

(deftest jupyterlab-version-test
  (with-redefs [sh (mock-sh {:exit 0 :out version-output})]
    (is (= 3
           (utils/jupyterlab-version "jupyter-test")))))
