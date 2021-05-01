;==================Checkers===============================================
;====Tensors====
(defn tensors-with-equal-size? [& tensors]
  (or (every? number? tensors)
      (and
        (every? vector? tensors)
        (apply = (mapv count tensors))
        (apply tensors-with-equal-size? (apply concat tensors)))))

(defn tensors? [& tensors] (every? tensors-with-equal-size? tensors))

(defn get_dims [t]
  (letfn [(get_dims_reverse [t]
            (if (vector? t)
              (conj (get_dims_reverse (first t)) (count t))
              []
              ))]
    (reverse (get_dims_reverse t))))

(defn check_dim [dim & tensors]
  (let [dims (mapv get_dims tensors)]
    (every? (partial = dim) (mapv count dims))))

;====Vectors====
(defn vectors-with-equal-size? [& vectors]
  (and (apply tensors-with-equal-size? vectors)
       (apply (partial check_dim 1) vectors)))

(defn vectors? [& vectors] (every? vectors-with-equal-size? vectors))

;====Matrices====
(defn matrices-with-equal-size? [& matrices]
  (and (apply tensors-with-equal-size? matrices)
       (apply (partial check_dim 2) matrices)))

(defn matrices? [& matrices] (every? matrices-with-equal-size? matrices))

;====Broadcast====
(defn prefix? [b1, b2]
  (let [a (get_dims b1) b (get_dims b2)
        n (count a) n2 (count b) m (min n n2)]
    (= (take m a) (take m b))))

(defn broadcasted? [& b]
  (let [comp (fn [b] (count (get_dims b)))
        sorted (sort-by comp b),
        biggest (last sorted)]
    (every? #(prefix? % biggest) sorted)))

(defn broadcast_impl [a, b_dims, cur]
  (if (= cur (count b_dims))
    a
    (if (vector? a)
      (mapv #(broadcast_impl % b_dims (inc cur)) a)
      (vec (repeat (nth b_dims cur) (broadcast_impl a b_dims (inc cur)))))))

(defn broadcast [& b]
  (let [comp (fn [b] (count (get_dims b)))
        sorted (sort-by comp b),
        biggest_dim (get_dims (last sorted))]
    (mapv #(broadcast_impl % biggest_dim 0) b)))

(defn true_broadcasts? [& broadcasts]
  (and
    (apply tensors? broadcasts)
    (apply broadcasted? broadcasts)
    (apply tensors-with-equal-size? (apply broadcast broadcasts))))

;==================Main program===============================================
;=========Tensors=========
(defn tensor-calc [fun & tensors]
  (if (vector? (first tensors))
    (apply mapv (partial tensor-calc fun) tensors)
    (apply fun tensors)))

(defn tensor-op [f checker]
  (fn [& args]
    {:pre [(apply checker args)]}
    (apply (partial tensor-calc f) args)))

(def t+ (tensor-op + tensors-with-equal-size?))
(def t- (tensor-op - tensors-with-equal-size?))
(def t* (tensor-op * tensors-with-equal-size?))
(def td (tensor-op / tensors-with-equal-size?))

;=========Vectors=========
(def v+ (tensor-op + vectors-with-equal-size?))
(def v- (tensor-op - vectors-with-equal-size?))
(def v* (tensor-op * vectors-with-equal-size?))
(def vd (tensor-op / vectors-with-equal-size?))

;=========Matrices=========
(def m+ (tensor-op + matrices-with-equal-size?))
(def m- (tensor-op - matrices-with-equal-size?))
(def m* (tensor-op * matrices-with-equal-size?))
(def md (tensor-op / matrices-with-equal-size?))

;=========Broadcasts=========
(defn broadcast-op [f]
  (fn [& args]
    {:pre [(apply true_broadcasts? args)]}
    (apply f (apply broadcast args))))

(def tb+ (broadcast-op t+))
(def tb- (broadcast-op t-))
(def tb* (broadcast-op t*))
(def tbd (broadcast-op td))

;=========Other Matrices & Vectors operations=========
(defn reduce-op [f] (fn [& args] (reduce f args)))

(defn v*s [v & scalars]
  {:pre [(and (vectors? v) (every? number? scalars))]}
  (mapv (partial * (reduce * scalars)) v))

(defn m*s [m & scalars]
  {:pre [(and (matrices? m) (every? number? scalars))]}
  (mapv #(v*s % (reduce * scalars)) m))

(defn scalar [& args]
  {:pre [(apply vectors-with-equal-size? args)]}
  (apply + (apply v* args)))

(def vect
  (reduce-op (fn [a, b]
               {:pre [(and (vectors-with-equal-size? a b) (== (count a) 3))]}
               (let [[ax, ay, az] a [bx, by, bz] b]
                 [(- (* ay bz) (* az by)), (- (* az bx) (* ax bz)), (- (* ax by) (* ay bx))]))))

(defn transpose [m]
  {:pre [(matrices? m)]}
  (apply mapv vector m))

(defn m*v [m, v]
  {:pre [(and (matrices? m) (vectors? v) (== (count (first m)) (count v)))]}
  (mapv (fn [a] (scalar a v)) m))

(def m*m
  (reduce-op (fn [m1, m2]
               {:pre [(and (matrices? m1 m2) (== (count (first m1)) (count m2)))]}
               (let [a m1 b (transpose m2)]
                 (mapv (fn [ai] (mapv (partial scalar ai) b)) a)))))
