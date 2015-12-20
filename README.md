# k-means

This repo contains code for two-component k-means. What is it you can read (in Russian)
in S.D. Dvoenko's article http://jmlda.org/papers/doc/2014/no9/Dvoenko2014Bipartial.pdf

## Installation

Clone the repo and run `lein repl`. Experimenting is about working in repl.

## Usage

Weigth is coefficient in quality function.
One part of the function is mean inner-cluster distance,
second is mean inter-cluster proximity.

Clustering with particular weight:

```
(def filename_amb "resources/data/owsinski/nested_2lvls_ambiguous.csv")
(-main filename_amb "1/8" "[45 3 2 2 2 2 2 2]")
(-main filename_amb "0" "[3 2 6 5 5 5 2 2 5 2 4 0 5 1 2 6 0 5 0 6 1 6 0 6 1 0 0 0 1 0 1 4 3 2 1 6 6 1 1 6 4 1 5 3 4 0 5 5 2 4 1 4 1 0 5 4 6 3 4 0]")
```

Functions to generate some useful weight secuences:

```
(uniform-weights 10)
; => [0 1/10 1/5 3/10 2/5 1/2 3/5 7/10 4/5 9/10 1]
(bino-weights 10)
; => [0 1/256 1/128 1/64 1/32 1/16 1/8 1/4 1/2 1]
(bino-tail-weights 10)
; => [0 1/2 3/4 7/8 15/16 31/32 63/64 127/128 255/256 1]
```

Convert sequence of weights to sequence of errors count:

```
(require '[k-means.clustering :as c])
(require '[incanter.core :as i])
(require '[incanter.chanrs :as ic])
(def right (c/template->clustering [7 8 8 7 8 7 7 8] matrix-data))
(def rs (errors-by-weight
         (c/template->clustering [30 3 2 5 5 5 5 5] matrix-data)
         (bino-weights 10)
         right))
; and draw the function f(weight) -> errors-count
(i/view (ic/scatter-plot (bino-weights 10) rs))
```


## License

Copyright © 2015 Sergey Smagin

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
