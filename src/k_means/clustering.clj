(ns k-means.clustering
  (:require [k-means.data-transform :as tr])
  (:use [incanter core stats charts]))

; numbers for cluster in dataset
; @param num [Integer/String] number/name/id of cluster
; @param clustering [Array[Integer/String]] clustering
; @return [Array[Integer]] indices in clustering array
;
(defn cluster-numbers [num clustering]
  (map first (filter #(= (second %) num) (map-indexed vector clustering))))

(defn cluster [num clustering data]
  (let [nums (cluster-numbers num clustering)]
  (sel data :rows nums :cols nums)))

(defn intercluster [num1 num2 clustering data]
  (let [nums1 (cluster-numbers num1 clustering)
        nums2 (cluster-numbers num2 clustering)]
    (sel data :rows nums1 :cols nums2)))

(defn weighted-dispersion [num clustering data]
  (let [c (cluster num clustering data)]
    (* (tr/eta c) (count c))))

(defn calculate-dispersion [clustering data]
  (let [weighted (map #(weighted-dispersion % clustering data) (distinct clustering))]
  ( / (reduce + weighted)
      (count clustering))))

(defn proximity [num1 num2 clustering data]
  (let [div (reduce + (flatten (intercluster num1 num2 clustering data)))
        den (* (count (cluster-numbers num1 clustering))
               (count (cluster-numbers num2 clustering)))]
    (/ div den)))

(defn calculate-proximity [clustering data]
  (let [ids (distinct clustering)
        den (pow (count ids) 2)]
    (/ (reduce 
         (fn [a id1]
           (+ a (reduce (fn [a' id2]
                          (+ a' (proximity id1 id2 clustering data))) ids)))
        ids) den)))

(defn quality [weight clustering distances proximities]
  (let [comp1 (calculate-proximity clustering proximities)
        comp2 (calculate-dispersion clustering distances)]
    (-(* (- 1 weight) comp2)
      (* weight comp1))))

(defn draw-clustering [clustering data]
  (let [x (sel data :cols 0)
        y (sel data :cols 1)
        sp (scatter-plot x y
                         :group-by clustering)]
  (view sp)))

(defn last? [num clustering]
  (= 1 (count (filter #{num} clustering))))

(defn new-cluster-number [id clustering weight distances proximities]
  (let [qual (fn [cluster-number]
               (quality weight
                        (assoc @clustering id cluster-number)
                        distances
                        proximities))]
    (if (last? (@clustering id) @clustering)
      (@clustering id)
      (apply min-key qual (distinct @clustering)))))

(defn try-move! [id clustering w d p]
  (let [r (new-cluster-number id clustering w d p)]
    (do
      (swap! clustering assoc id r)
      r)))

(defn clusterize-iteration! [clustering weight distances proximities]
  (map
    #(try-move! % clustering weight distances proximities)
    (range (count @clustering))))

(defn template->clustering [template clusters-count matrix-data]
  (let [objects-count (length matrix-data)]
        ; todo: logic for init clustering
        ))
