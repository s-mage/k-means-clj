;; gorilla-repl.fileformat = 1

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns ara.practice2
  (:require [gorilla-plot.core :as plot])
  (:use [incanter core io charts distributions]
        [incanter-gorilla.render]
        [gorilla-repl table html]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def a_min 15)
(def a_max 30)
(def b_min 250)
(def b_max 350)
(def p_1 0.5)
(def p_2 0.05)
(def p_3 0.5)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;ara.practice2/p_3</span>","value":"#'ara.practice2/p_3"}
;; <=

;; **
;;; Часть 1
;;; -------
;;; 
;;; Математические ожидания распределений: 
;;; 
;;; $$ M(c) = λ = M(a) p_1 + M(b) p_2 $$
;;; $$ M(d) = M(M(c) + B(M(c), p_3)) = M(c) + M(c) \cdot 0.5 = 1.5 M(c) $$
;; **

;; @@
(let [ea (/ (+ a_min a_max) 2)
      eb (/ (+ b_min b_max) 2)
      ec (+ (* m_a p_1) (* m_b p_2))]
  (def expected-value
    {:a (int ea) :b (int eb) :c (int ec) :d (int (* 1.5 ec))}))

(prn expected-value)
;; @@
;; ->
;;; {:a 22, :b 300, :c 26, :d 39}
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Дисперсии распределений:
;;; ------------------------
;;; 
;;; $$ D(a) = ((a_{max} - a_min)^2) / 12 $$
;;; $$ D(c) = \lambda = M(c) $$
;;; $$ D(d) = M(c) \cdot p_3 \cdot (1 - p_3) = M(c) * 0.25 = \frac{M(c)}{4}$$ 
;; **

;; @@
(def dispersion {
  :a (int (/ (pow (- a_max a_min) 2) 12))
  :b (int (/ (pow (- b_max b_min) 2) 12))
  :c (expected-value :c)
  :d (int (/ (expected-value :c) 4))})
(prn dispersion)
;; @@
;; ->
;;; {:a 18, :b 833, :c 26, :d 6}
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@

;; @@
