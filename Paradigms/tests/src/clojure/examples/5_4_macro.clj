(chapter "Parser macro")

(section "Parser macro definition")
(example "-collect rules"
         (defn +collect [defs]
           (cond
             (empty? defs) ()
             (seq? (first defs)) (let [[[key args body] & tail] defs]
                                   (cons
                                     {:key key :args args :body body}
                                     (+collect tail)))
             :else (let [[key body & tail] defs]
                     (cons
                       {:key key :args [] :synth true :body body}
                       (+collect tail))))))
(example "parser macro"
         (defmacro defparser [name & rules]
           (let [collected (+collect rules)
                 keys (set (map :key (filter :synth collected)))]
             (letfn [(rule [{key :key, args :args, body :body}] `(~key ~args ~(convert body)))
                     (convert [value]
                       (cond
                         (seq? value) (map convert value)
                         (char? value) `(+char ~(str value))
                         (keys value) `(~value)
                         :else value))]
               `(def ~name (letfn ~(mapv rule collected) (+parser (~(:key (last collected))))))))))

(section "Parser macro usage example")
(example "JSON parser"
         (defparser json
                     *null (+seqf (constantly nil) \n\u\l\l)
                     *all-chars (mapv char (range 0 128))
                     *letter (+char (apply str (filter #(Character/isLetter %) *all-chars)))
                     *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
                     *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars)))
                     *ws (+ignore (+star *space))
                     *number (+map read-string (+str (+plus *digit)))
                     *identifier (+str (+seqf cons *letter (+star (+or *letter *digit))))
                     *string (+seqn 1 \" (+str (+star (+char-not "\""))) \")
                     (*seq [begin p end]
                           (+seqn 1 begin (+opt (+seqf cons *ws p (+star (+seqn 1 *ws \, *ws p)))) *ws end))
                     *array (+map vec (*seq \[ (delay *value) \]))
                     *member (+seq *identifier *ws (+ignore \:) *ws (delay *value))
                     *object (+map (partial reduce #(apply assoc %1 %2) {}) (*seq \{ *member \}))
                     *value (+or *null *number *string *object *array)
                     *json (+seqn 0 *ws *value *ws))
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]")
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]~"))
