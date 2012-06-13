(ns stack
  (:use [belt.hash-maps :only [update]]
        [belt.general-utils :only [casep
                                   boolean?]]))

(def all-types
  #{:int :bool :char :string :exec :type})

(def empty-stack
  (zipmap all-types (repeat ())))

(defn stack-type
  [obj]
  (casep obj
    integer? :int
    boolean? :bool
    char? :char
    string? :string
    keyword? :type))

(defn get-arg
  [stack arg-type]
  (let [arg (first (stack arg-type))]
    (update stack
            :arg-temp #(cons arg %)
            arg-type rest)))