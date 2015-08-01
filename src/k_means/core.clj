(ns k-means.core
  (:gen-class)
  (:require [k-means.clustering :as c]
            [k-means.data-transform :as tr])
  (:use [incanter core io]))

(declare data matrix-data distances proximities clustering)

(defn clusterize!
  ([weight]
   (clusterize!
     weight
     @clustering
     (c/clusterize-iteration! clustering weight distances proximities)))
  ([weight old new]
   (when-not (= old new)
     (recur
       weight
       new
       (c/clusterize-iteration! clustering weight distances proximities)))))

(defn -main
  "Get filename and weight, clusterize space and draw the result"
  ([filename weight] (-main filename weight "[7 8 7 8 7 8 7 8]"))
  ([filename weight initial-template]
  (def data (read-dataset filename :header false))
  (def matrix-data (to-matrix data))
  (def distances (tr/data->distances matrix-data))
  (def proximities (tr/distances->proximities distances))
  (def clustering
    (atom (vec
      (flatten (map-indexed #(repeat %2 %1) (read-string initial-template))))))
  (c/draw-clustering @clustering matrix-data)
  (clusterize! (read-string weight))
  (prn @clustering)
  (c/draw-clustering @clustering matrix-data)))
