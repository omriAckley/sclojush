(ns core
  (:use [belt.hash-maps :only [update
                               remove-hash-map]]
        [belt.general-utils :only [%->]]
        clojure.walk
        instructions
        stack
        slingshot.slingshot))

(def demo? (atom false))

;;Uncomment the following to run a sclojush program
#_(reset! demo? true)
#_(eval-program '(1 10 (20 4 :int) +))

(defmacro when-demo
  [& exprs]
  `(when @demo? ~@exprs))

(def execution-limit 10)

(def execution-count (atom 0))

(defn handle-execution-exception
  [msg stack]
  (if (= "Execution limit reached." msg)
    stack
    (throw (Exception. msg))))

(defn push
  ([stack obj]
    (cond
      (nil? obj) stack
      (coll? obj) (reduce push stack obj)
      :else (push stack
                  obj
                  (stack-type obj))))
  ([stack obj type]
    (when-demo (println "  Pushing:" obj type))
    (let [new-stack
          (if (nil? obj)
            stack
            (update stack
                    type #(cons obj %)))]
      (when-demo (println "  New stack:" new-stack))
      new-stack)))

(defn call-instruction
  [stack instruction]
  (if-not (contains? arglist-map instruction)
    (throw+ [(str "'" instruction "'"
                  " is not a recognized sclojush instruction."
                  " Please update 'arglist-map' in the 'instructions' namespace.")
             stack])
    (try
      (let [arglist (arglist-map instruction)
            [args
             new-stack] (if-not (coll? arglist)
                          [(stack arglist) (assoc stack arglist ())]
                          (reduce get-arg
                                  stack
                                  arglist))
            return (apply (eval instruction) args)]
        (when-demo (println "  Doing:" (list* instruction args)))
        (push new-stack return))
      (catch Exception _ stack))))

(declare eval-expression)
(defn execute
  [stack object]
  (swap! execution-count inc)
  (when-demo
    (println "At:" object)
    (println "  Stack:" stack))
  (if (> @execution-count execution-limit)
    (throw+ ["Execution limit reached." stack]))
  (cond
    (seq? object) (eval-expression stack object)
    (fn? (try
           (eval object)
           (catch Exception e
                  (throw+ [(.getMessage e) stack])))) (call-instruction stack object)
    :else (push stack object)))

(defn eval-expression
  ([expr]
    (eval-expression empty-stack expr))
  ([initial-stack expr]
    (let [full-stack (try+
                       (reduce execute
                               initial-stack
                               expr)
                       (catch Object [msg stack]
                              (handle-execution-exception msg stack)))
          higher-stack-merges (select-keys initial-stack
                                           (full-stack :type))]
      (when-demo
        (println "At:" "END OF" expr)
        (println "  Stack:" full-stack)
        (println (apply str (cons "  Ignoring changes: "
                                  (distinct (full-stack :type)))))
        (println "  New stack:" (merge (dissoc full-stack :type)
                                       higher-stack-merges)))
      (%-> full-stack
           (dissoc % :type)
           (merge % higher-stack-merges)
           (remove-hash-map empty? %)))))

(defmacro eval-program
  ([prog]
    `(eval-program [] ~empty-stack ~prog))
  ([bindings prog]
    `(eval-program ~bindings ~empty-stack ~prog))
  ([bindings initial-stack prog]
    (reset! execution-count 0)
    `(let ~bindings
       (eval-expression ~initial-stack ~prog))))
