(ns k-means.core
  (:gen-class)
  (:require [incanter.core :as i]
            [incanter.io :as iio]
            [k-means.clustering :as c]
            [k-means.data-transform :as tr]))

(declare data matrix-data distances proximities clustering)

(defn clusterize
  ([weight clustering-atom]
   (clusterize
     weight
     @clustering-atom
     (c/clusterize-iteration! clustering-atom weight distances proximities)
     clustering-atom))
  ([weight old new clustering-atom]
   (when-not (= old new)
     (recur
       weight
       new
       (c/clusterize-iteration! clustering-atom weight distances proximities)
       clustering-atom))))

(defn errors-count [wrong right])

(defn best-clustering [initial attempts right]
  "Find the best clustering for weight in range (0..1) with 1 / <attempts> step"
  (let [atoms (pmap (fn [weight] [(atom initial) (/ weight attempts)]) (range attempts))
        clusterized (pmap (fn [weight clustering] (clusterize weight clustering)) atoms)
        best (max-key #(errors-count % right) clusterized)]
    best))

(defn -main
  "Get filename and weight, clusterize space and draw the result"
  ([filename weight] (-main filename weight "[7 8 7 8 7 8 7 8]"))
  ([filename weight initial-template]
  (def data (iio/read-dataset filename :header false))
  (def matrix-data (i/to-matrix data))
  (def distances (tr/data->distances matrix-data))
  (def proximities (tr/distances->proximities distances))
  (def clustering
    (atom (c/template->clustering (read-string initial-template) matrix-data)))
  (c/draw-clustering @clustering data)
  (clusterize (read-string weight) clustering)
  (prn @clustering)
  (c/draw-clustering @clustering data)))
