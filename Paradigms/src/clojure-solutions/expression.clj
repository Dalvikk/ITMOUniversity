(defn variable [name] (fn [map] (map name)))
(def constant constantly)

(defn create-op [fun]
  (fn [& args]
    (fn [map] (apply fun (mapv #(% map) args)))))

(def add (create-op +))
(def subtract (create-op -))
(def negate subtract)
(def multiply (create-op *))
(def divide (create-op (fn
                         ([f] (/ (double 1) f))
                         ([f & a] (/ (double f) (apply * a))))))

(defn square-impl [& args] (mapv #(* % %) args))
(def square (create-op square-impl))

(defn mean-impl [& args] (/ (apply + args) (count args)))
(def mean (create-op mean-impl))

(def varn (create-op
            (fn [& args]
              (let [m (apply mean-impl args)
                    e (mapv #(- % m) args)
                    sq (apply square-impl e)]
                (apply mean-impl sq)))))

(def functional-op
  {'+      add,
   '-      subtract,
   '*      multiply,
   '/      divide,
   'negate negate,
   'mean   mean,
   'varn   varn,
   })

(defn parse [s constant-factory variable-factory operations-factory]
  (letfn [(_parse [s]
            (cond
              (number? s) (constant-factory s)
              (symbol? s) (variable-factory (str s))
              (list? s) (apply (operations-factory (first s)) (mapv _parse (rest s)))
              ))]
    (_parse s)))
(defn parseFunction [expression] (parse (read-string expression) constant variable functional-op))

;==========================HW11==========================
(load-file "proto.clj")

;=================Sugar=================

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

; :NOTE: не нужно определять снаружи
(def args (field :args))
(def eval-impl (field :eval-impl))
(def diff-impl (field :diff-impl))

;=================Factories=================
(defn common-expression-factory
  [evaluate toString diff]
  {:evaluate evaluate
   :toString toString
   :diff     diff})

(def null-ary-prototype
  (common-expression-factory
    (fn [this args] ((eval-impl this) this args))
    (fn [this] (str (:x this)))
    (fn [this var] ((diff-impl this) this var))))

(defn null-ary-factory
  [eval-rule diff-rule]
  (constructor
    (fn [this x] (assoc this
                   :x x
                   :eval-impl eval-rule
                   :diff-impl diff-rule))
    null-ary-prototype))

(def operation-prototype
  (common-expression-factory
    (fn [this values] (apply (eval-impl this) (mapv #(evaluate % values) (args this))))
    (fn [this] (str "(" (:token this) " " (clojure.string/join " " (mapv toString (args this))) ")"))
    (fn [this var] ((diff-impl this) (args this) (mapv #(diff % var) (args this))))))

(defn operation-factory
  [token eval-rule diff-rule]
  (constructor
    (fn [this & args] (assoc this
                        :args args
                        :token token
                        :eval-impl eval-rule
                        :diff-impl diff-rule))
    operation-prototype))

;=================Constant & Variable=================
; :NOTE: ZERO
(declare zero)
(def Constant
  (null-ary-factory
    (fn [this _] (:x this))
    (fn [_ _] zero)))

(def zero (Constant 0.0))
(def one (Constant 1.0))

(def Variable
  (null-ary-factory
    (fn [this vars] (vars (:x this)))
    (fn [this var]
      (cond (= (:x this) var) one :else zero))))

;=================Non null-ary operations=================

(def divide-impl (fn
                   ([f] (/ (double 1) f))
                   ([f & a] (/ (double f) (apply * a)))))

(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Square)

;(x1 * x2 * .. xn)' = (x1' * x2 .. xn) + (x1 * x2' .. xn) + ... + (x1 * x2 ... xn')
(def multiply-diff-impl (fn [args args']
                          (let [a (vec args)]
                            (letfn [(term [n] (apply Multiply (assoc a n (nth args' n))))]
                              (apply Add (mapv term (range (count args))))))))

(def divide-diff-impl (fn [args args']
                        (let [[x & tail] args [x' & tail'] args'
                              y (apply Multiply tail) y' (multiply-diff-impl tail tail')]
                          (if (empty? tail)
                            ; -1 * 1/x^2 * x'
                            (Divide (Multiply (Constant -1.0) x') (Square x))
                            ; (x / y)' = (x'y - y'x) / (y^2)
                            (Divide (Subtract (Multiply x' y) (Multiply y' x)) (Square y))))))

(def Add (operation-factory '+ + #(apply Add %2)))
(def Subtract (operation-factory '- - #(apply Subtract %2)))
(def Negate (operation-factory 'negate - #(apply Subtract %2)))
(def Multiply (operation-factory '* * multiply-diff-impl))
(def Divide (operation-factory '/ divide-impl divide-diff-impl))
(def Square (operation-factory 'square #(* % %) #(Multiply (Constant 2.0) (nth % 0) (nth %2 0))))

;=================Modification=================
(def Sign (operation-factory 'sign #(if (> % 0) 1 -1) (fn [_ _] (zero))))
(def Abs (operation-factory 'abs #(Math/abs ^double %) #(Multiply (Sign (first %) (first %2)))))
(def Log (operation-factory 'log #(Math/log %) #(Divide one (first %))))

(declare Pow)

(def pow-diff-impl (fn [a a']
                     (let [[x y] a [x' y'] a']
                       (Multiply
                         (Pow x (Subtract y one))
                         (Add (Multiply y x') (Multiply x y' (Log x)))))))

(def Pow (operation-factory 'pow #(Math/pow % %2) pow-diff-impl))

(def ArithMean (operation-factory 'arith-mean
                                  #(/ (apply + %&) (count %&))
                                  #(apply ArithMean %2)))

(def GeomMean (operation-factory 'geom-mean
                                 #(Math/pow (Math/abs ^double (apply * %&)) (/ (count %&)))
                                 #(let [mul (apply Multiply %)
                                        mul' (multiply-diff-impl % %2)
                                        n (/ (count %))]
                                    (Multiply (Sign mul) (Constant (/ n)) mul' (Pow (Abs mul) (Constant (dec n)))))))

(def HarmMean (operation-factory 'harm-mean
                                 #(divide-impl (count %&) (apply + (mapv divide-impl %&)))
                                 ; n * (x1'/x1^2 + x2'/x2^2 + ...) / (1/x1 + 1/x2 + ...)^ 2
                                 #(let [num (apply Add (mapv (fn [x x'] (Divide x' (Square x))) % %2))
                                        denom (Square (apply Add (mapv (fn [a] (Divide one a)) %)))]
                                    (Divide (Multiply (Constant (count %)) num) denom))))

;=================Parser=================
(def object-op
  {'+          Add
   '-          Subtract
   '*          Multiply
   '/          Divide
   'negate     Negate
   'arith-mean ArithMean
   'geom-mean  GeomMean
   'harm-mean  HarmMean
   })

(defn parseObject [s] (parse (read-string s) Constant Variable object-op))
