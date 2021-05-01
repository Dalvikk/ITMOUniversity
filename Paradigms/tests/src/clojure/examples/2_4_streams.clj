(chapter "Streams")

(section "Definitions")
(example "Setup namespace"
         (in-ns 'info.kgeorgiy.streams)
         (clojure.core/refer 'clojure.core :only '[defn refer alias let cond force delay = + - pos? println])
         (refer 'user :only '[example])
         (alias 'c 'clojure.core))
(example "Base definitions"
         (defn cons [head tail] [head tail])
         (defn first [[head _]] head)
         (defn rest [[_ tail]] (force tail))
         (def empty nil)
         (defn empty? [stream] (= empty stream)))
(example "Basic functions"
         (defn count [stream]
           (if (empty? stream)
             0
             (+ 1 (count (rest stream)))))
         (defn to-list [stream]
           (if (empty? stream)
             ()
             (c/cons (first stream) (to-list (rest stream))))))
(example "Map and Filter"
         (defn map [f stream]
           (if (empty? stream)
             empty
             (cons (f (first stream)) (delay (map f (rest stream))))))
         (defn filter [p? stream]
           (if (empty? stream)
             empty
             (let [head (first stream)
                   tail (delay (filter p? (rest stream)))]
               (if (p? head)
                 (cons head tail)
                 (force tail))))))
(example "Take and Take While"
         (defn take [n stream]
           (cond
             (empty? stream) empty
             (pos? n) (cons (first stream) (delay (take (- n 1) (rest stream))))
             :else empty))
         (defn take-while [p? stream]
           (cond
             (empty? stream) empty
             (p? (first stream)) (cons (first stream) (delay (take-while p? (rest stream))))
             :else empty)))
(example "Some and Every"
         (defn some [p? stream]
           (cond
             (empty? stream) false
             (p? (first stream)) true
             :else (some p? (rest stream))))
         (defn every [p? stream]
           (cond
             (empty? stream) true
             (p? (first stream)) (every p? (rest stream))
             :else false)))
(example "Leaving namespace"
         (in-ns 'user))

(section "Usage")
(example "Use namespace"
         (alias 'ks 'info.kgeorgiy.streams))
(example "Finite streams"
         (ks/empty? ks/empty)
         (ks/empty? (ks/cons 1 ks/empty))
         (def s123 (ks/cons 1 (ks/cons 2 (ks/cons 3 ks/empty))))
         s123
         (ks/count ks/empty)
         (ks/count s123)
         (ks/to-list s123)
         (ks/map #(+ % %) s123)
         (ks/to-list (ks/map #(+ % %) s123))
         (ks/to-list (ks/filter odd? s123))
         (ks/count (ks/take 2 s123))
         (ks/to-list (ks/take 2 s123))
         (ks/to-list (ks/take-while (partial >= 2) s123))
         (ks/some (partial = 2) s123)
         (ks/every (partial = 4) s123))
(example "Infinite streams"
         (defn stream-sample [stream] (ks/to-list (ks/take 30 stream)))
         (def stream-ones (ks/cons 1 (delay stream-ones)))
         (stream-sample stream-ones)
         (defn stream-integers [i] (ks/cons i (delay (stream-integers (+ i 1)))))
         (stream-sample (stream-integers 0))
         (def primes
           (letfn [(prime? [n] (not (ks/some #(== 0 (mod n %)) (ks/take-while #(>= n (* % %)) (prs)))))
                   (prs [] (ks/cons 2 (delay (ks/filter prime? (stream-integers 3)))))]
             (prs)))
         (stream-sample primes)
         (stream-sample (ks/map (partial * 10) primes)))
(example "Lazy sequences"
         (defn lazy-sample [seq] (apply list (take 30 seq)))
         (lazy-sample (letfn [(lazy-ones [] (cons 1 (lazy-seq (lazy-ones))))] (lazy-ones)))
         (defn lazy-integers [i] (cons i (lazy-seq (lazy-integers (+ i 1)))))
         (lazy-sample (lazy-integers 0))
         (def lazy-primes
           (letfn [(prime? [n] (not (some #(= 0 (mod n %)) (take-while #(>= n (* % %)) (prs)))))
                   (prs [] (cons 2 (filter prime? (lazy-integers 3))))]
             (prs)))
         (lazy-sample lazy-primes))
(example "Lazy input"
         (defn lazy-input []
           (let [line (read-line)]
             (if (not (or (empty? line) (= "." line)))
               (cons line (lazy-seq (lazy-input))))))
         (defn with-lazy-input [init f]
           (fn [] (reduce f init (lazy-input)))))
(example "Running sum with lazy input"
         (def running-sum
           (with-lazy-input
             0
             (fn [sum line]
               (let [sum' (+ sum (read-string line))]
                 (do
                   (println sum')
                   sum')))))
         (with-in-file "data/sum.in" running-sum))
