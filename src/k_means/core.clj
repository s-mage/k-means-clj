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
   (if (= old new)
     clustering-atom
     (recur
       weight
       new
       (c/clusterize-iteration! clustering-atom weight distances proximities)
       clustering-atom))))


(defn uniform-weights [attempts]
  (mapv #(/ % attempts) (range (+ attempts 1))))

(defn bino-tail-weights [attempts]
  (let [summands (reduce (fn [r x] (conj r (/ (last r) 2))) [1/2] (range (- attempts 3)))]
    (conj (reduce (fn [r, x] (conj r (+ (last r) x))) [0] summands) 1)))

(defn bino-weights [attempts]
  (let [summands (reduce (fn [r x] (conj r (/ (last r) 2))) [1/2] (range (- attempts 3)))]
    (conj (vec (reverse (conj summands 0))) 1)))

(defn errors-count [wrong right]
  (count (filter #(false? %) (mapv = wrong right))))

(defn errors-by-weight
  "Find the best clustering for weight in range (0..1) with 1 / <attempts> step"
  [initial weights right]
  (let [clusterized (pmap (fn [w] (clusterize w (atom initial))) weights)]
    (map #(errors-count @% right) clusterized)))

(defn best-clustering
  "Find the best clustering for weight in range (0..1) with 1 / <attempts> step"
  [initial weights right]
  (apply min-key last (map-indexed (fn [k v] [v (weights k)])
                                   (errors-by-weight initial weights right))))


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
