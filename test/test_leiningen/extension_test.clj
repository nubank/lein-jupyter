(ns test-leiningen.extension-test 
  (:require
   [clojure.test :refer [deftest is testing]]
   [leiningen.jupyter.extension :as extension]))

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
