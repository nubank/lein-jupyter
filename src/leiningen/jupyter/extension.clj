(ns leiningen.jupyter.extension
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :refer [sh]]
   [leiningen.core.main :as leiningen.main]
   [leiningen.jupyter.params :as params]
   [leiningen.jupyter.utils :as utils])
  (:import
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

(defmulti enable-extension* (fn [major-version & _args] major-version))

(defmethod enable-extension* 3
  [_ jupyter-ex extension-main]
  (sh jupyter-ex "nbextension" "enable" extension-main "--user"))

(defmethod enable-extension* 4
  [_ _jupyter-ex extension-main]
  (sh "jupyter-nbclassic-extension" "enable" extension-main "--user"))

(defn enable-extension
  "enable the lein-jupyter-parinfer extension the user space"
  [jupyter-version jupyter-exe]
  (let [enable-out (enable-extension* jupyter-version jupyter-exe "lein-jupyter-parinfer/index")]
    (if (not= 0 (:exit enable-out))
      (leiningen.main/warn "Did not succeed to enable lein-jupyter-parinfer extension"
                           (:err enable-out))
      true)))

(defmulti install-extension* (fn [major-version & _args] major-version))

(defmethod install-extension* 3
  [_ jupyter-exe extension-dir]
  (sh jupyter-exe "nbextension" "install" extension-dir "--user"))

(defmethod install-extension* 4
  [_ _jupyter-exe extension-dir]
  (sh "jupyter-nbclassic-extension" "install" extension-dir "--user"))

(defn install-extension
  "Instal the lein-jupyter-parinfer extension the user space"
  [jupyter-version jupyter-exe]
  (let [extension-dir (copy-resource-dir-in-tmp-dir "lein-jupyter-parinfer")
        install-out (install-extension* jupyter-version jupyter-exe (.getCanonicalPath extension-dir))]
    (if (not= 0 (:exit install-out))
      (leiningen.main/warn "Did not succeed to install lein-jupyter-parinfer extension"
                           (:err install-out))
      true)))

(defn install-and-enable-extension [project]
  (let [jupyter-exe (params/jupyer-executable project)
        jupyter-version (utils/jupyterlab-version jupyter-exe)]
    (and (install-extension jupyter-version jupyter-exe)
         (enable-extension jupyter-version jupyter-exe))))
