(ns leiningen.jupyter.utils
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.string :as string]))

(defn ^:private run-jupyter-versions-cmd
  [jupyter-exec]
  (let [{:keys [exit out]} (sh jupyter-exec "--version")]
    (when (= 0 exit)
      out)))

(defn jupyter-versions
  "Run the jupyter --version in a shell, and return a map with
  the result.

  E.g:
  {\"nbformat\"       \"5.9.2\"
   \"jupyter_core\"   \"5.3.0\"
   \"notebook\"       \"not installed\"
   \"jupyterlab\"     \"4.0.5\"
   \"jupyter_server\" \"2.7.3\"}"
  [jupyter-exec]
  (->> (run-jupyter-versions-cmd jupyter-exec)
       (#(clojure.string/split % #"\n"))
       (drop 1)                                   ; Selected Jupyter core packages...
       (map #(clojure.string/split % #"\s+:\s+")) ; jupyterlab       : 4.0.5
       flatten
       (apply hash-map)))

(defn to-int
  [s]
  (try
    (Integer/parseInt s)
    (catch Exception _ nil)))

(defn major-version
  [version]
  (some-> version
          (string/split #"\.")
          first
          to-int))

(defn jupyterlab-version
  [jupyter-exe]
  (-> jupyter-exe
      jupyter-versions
      (get "jupyterlab")
      major-version))
