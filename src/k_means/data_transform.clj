(ns k-means.data-transform
  (:require [clojure.core.matrix :as m]
            [incanter.core :as i]))

(defn distance [vec1 vec2]
  (-> (m/sub vec1 vec2)
      (m/length-squared)
      (Math/sqrt)))

(defn data->distances [matrix-data]
  (map (fn [v1] (map #(distance v1 %) matrix-data)) matrix-data))

(defn eta [mat]
  (let [n (i/nrow mat)
        num (m/length-squared (i/vectorize mat))
        den (* 2 (Math/pow n 2))]
  (/ num den)))

(defn distances->proximities [distances]
  (let [N (i/nrow distances)
        eta (eta distances)
        dist2origin (fn [vec] (- (/ (m/length-squared vec) N) eta))
        d2o (map dist2origin distances)
        d->p (fn [id1 id2 d1 d2]
               (- (+ d1 d2)
                  (/ (Math/pow (i/sel distances id1 id2) 2) 2)))]
    (i/matrix
      (map-indexed
        (fn [id1 d1] map-indexed (fn [id2 d2] (d->p id1 id2 d1 d2)) d2o)
        d2o))))
