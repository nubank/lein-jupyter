(ns leiningen.jupyter.extension
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :refer [sh]]
   [leiningen.core.main :as leiningen.main]
   [leiningen.jupyter.params :as params]
   [leiningen.jupyter.utils :as utils])
  (:import
   [java.io IOException]
   [java.nio.file Files]
   [java.nio.file.attribute FileAttribute]))


(def resources
  (map io/file ["lein-jupyter-parinfer/README.md"
                "lein-jupyter-parinfer/index.js"
                "lein-jupyter-parinfer/parinfer-codemirror.js"
                "lein-jupyter-parinfer/parinfer.js"]))

(defn get-resource-by-name [n]
  (slurp (io/resource (str n))))

(defn move-resource [dest content]
  (io/make-parents dest)
  (spit dest content))

(defn copy-resource-dir-in-tmp-dir [name]
  (let [root-tmp-dir (.toFile (Files/createTempDirectory "lein-jupyter" (into-array FileAttribute [])))
        tmp-dir (io/file root-tmp-dir name)]
    (doall (map #(move-resource (io/file tmp-dir (.getName %))  (get-resource-by-name %)) resources))
    tmp-dir))

(defn nbextension-cmd
  [major-version jupyter-ex & subcommand]
  (if (and major-version (>= major-version 4))
    (concat ["jupyter-nbclassic-extension"] subcommand)
    (concat [jupyter-ex "nbextension"] subcommand)))

(defn enable-extension-sh
  [major-version jupyter-ex extension-main]
  (apply sh (nbextension-cmd major-version jupyter-ex "enable" extension-main "--user")))

(defn enable-extension
  "enable the lein-jupyter-parinfer extension the user space"
  [jupyter-version jupyter-exe]
  (let [enable-out (enable-extension-sh jupyter-version jupyter-exe "lein-jupyter-parinfer/index")]
    (if (not= 0 (:exit enable-out))
      (leiningen.main/warn "Did not succeed to enable lein-jupyter-parinfer extension"
                           (:err enable-out))
      true)))

(defn install-extension-sh
  [major-version jupyter-exe extension-dir]
  (let [install-ex-cmd (nbextension-cmd major-version jupyter-exe "install" extension-dir "--user")]
    (try
      (apply sh install-ex-cmd)
      (catch IOException ex
        (leiningen.main/warn "Did not succeed to run extension installer:" install-ex-cmd ex)
        {:exit -1 :err (.getMessage ex)}))))

(defn install-extension
  "Instal the lein-jupyter-parinfer extension the user space"
  [jupyter-version jupyter-exe]
  (let [extension-dir (copy-resource-dir-in-tmp-dir "lein-jupyter-parinfer")
        install-out (install-extension-sh jupyter-version jupyter-exe (.getCanonicalPath extension-dir))]
    (if (not= 0 (:exit install-out))
      (leiningen.main/warn "Did not succeed to install lein-jupyter-parinfer extension"
                           (:err install-out))
      true)))

(defn install-and-enable-extension [project]
  (let [jupyter-exe (params/jupyer-executable project)
        jupyter-version (utils/maybe-jupyterlab-version jupyter-exe)]
    (and (install-extension jupyter-version jupyter-exe)
         (enable-extension jupyter-version jupyter-exe))))
