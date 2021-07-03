(chapter "Combinators")

(section "Syntactic sugar")
(example "+char: single character in string"
         (defn +char [chars]
           (_char (set chars)))
         (tabulate (+char "abc") ["a" "a~" "b" "b~" "" "x" "x~"]))
(example "+char-not: single character not in string"
         (defn +char-not [chars]
           (_char (comp not (set chars))))
         (tabulate (+char-not "abc") ["a" "a~" "b" "b~" "" "x" "x~"]))
(example "+map: map result"
         (defn +map [f parser]
           (comp (partial _map f) parser))
         (tabulate (+map #(Character/toUpperCase %) (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"]))
(example "import"
         (def +parser _parser))

(section "Sequences")
(example "+ignore: ignore result"
         (def +ignore
           (partial +map (constantly 'ignore)))
         (tabulate (+ignore (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"]))
(example "+seq: sequence"
         (defn iconj [coll value]
           (if (= value 'ignore)
             coll
             (conj coll value)))
         (defn +seq [& ps]
           (reduce (partial _combine iconj) (_empty []) ps))
         (tabulate (+seq (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~" "aA" ""]))
(example "+seqf: sequence with function"
         (defn +seqf [f & ps]
           (+map (partial apply f) (apply +seq ps)))
         (tabulate (+seqf str (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"]))
(example "+seqn: n-th element of the sequence"
         (defn +seqn [n & ps]
           (apply +seqf #(nth %& n) ps))
         (tabulate (+seqn 1 (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"]))

(section "Alternatives")
(example "+or: first matching parser"
         (defn +or [p & ps]
           (reduce _either p ps))
         (tabulate (+or (+char "a") (+char "b") (+char "c")) ["a" "a~" "b" "b~" "" "x" "x~"]))
(example "+opt: optional parser"
         (defn +opt [p]
           (+or p (_empty nil)))
         (tabulate (+opt (+char "a")) ["a" "a~" "aa" "aa~" "" "~"]))
(example "+star: repeated parser"
         (defn +star [p]
           (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))]
             (rec)))
         (tabulate (+star (+char "a")) ["a" "a~" "aa" "aa~" "" "~"]))
(example "+plus: at least one repeat parser"
         (defn +plus [p]
           (+seqf cons p (+star p)))
         (tabulate (+plus (+char "a")) ["a" "a~" "aa" "aa~" "" "~"]))
(example "+str: convert to string"
         (defn +str [p] (+map (partial apply str) p))
         (tabulate (+str (+star (+char "a"))) ["a" "a~" "aa" "aa~" "" "~"]))
