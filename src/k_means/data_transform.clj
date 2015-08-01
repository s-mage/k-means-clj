(ns k-means.data-transform
  (:use [incanter core stats]))

(defn distance [vec1 vec2]
  (-> (minus vec1 vec2)
      (#(pow % 2))
      (sum)
      (Math/sqrt)))

(defn data->distances [matrix-data]
  (map (fn [v1] (map #(distance v1 %) matrix-data)) matrix-data))

(defn eta [mat]
  (let [n (count mat)
        squared (map #(pow % 2) (flatten mat))
        num (reduce + squared)
        den (* 2 (pow n 2))]
  (/ num den)))

(defn distances->proximities [distances]
  (let [N (count distances)
        eta (/ (reduce + (map #(pow % 2) (flatten distances))) (* 2 (pow N 2)))
        dist2origin (fn [vec] (- (/ (sum (pow vec 2)) N) eta))
        d2o (map dist2origin distances)
        d->p (fn [id1 id2 d1 d2]
               (- (+ d1 d2)
                  (/ (pow (sel distances id1 id2) 2) 2)))]
    (matrix
      (map-indexed
        (fn [id1 d1] map-indexed (fn [id2 d2] (d->p id1 id2 d1 d2)) d2o)
        d2o))))
