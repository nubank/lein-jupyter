(ns test-leiningen.extension-test 
  (:require
   [clojure.test :refer [deftest is testing]]
   [leiningen.jupyter.extension :as extension]))

(deftest extract-resource-test
    (testing "the copy of the resources"
      (let [resource "lein-jupyter-parinfer"
            ret (extension/copy-resource-dir-in-tmp-dir resource)]
        (is (= (.getName ret) "lein-jupyter-parinfer"))
        (is (= (count (.listFiles ret)) 4)))))

(deftest nbextension-cmd-test
  (testing "Major version 3 and before should use jupyter jupyter nbextension"
    (is (= ["jupyter" "nbextension" "my-command" "--arg" "xpto"]
           (extension/nbextension-cmd 3 "jupyter" "my-command" "--arg" "xpto")))

    (is (= ["jupyter" "nbextension" "my-command" "--arg" "xpto"]
           (extension/nbextension-cmd 2 "jupyter" "my-command" "--arg" "xpto"))))

  (testing "Major version 4 and beyond should use jupyter nb-classic-extension without calling jupyter"
    (is (= ["jupyter-nbclassic-extension" "my-command" "--arg" "xpto"]
           (extension/nbextension-cmd 4 "jupyter" "my-command" "--arg" "xpto")))

    (is (= ["jupyter-nbclassic-extension" "my-command" "--arg" "xpto"]
           (extension/nbextension-cmd 5 "jupyter" "my-command" "--arg" "xpto")))))
