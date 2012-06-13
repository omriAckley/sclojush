(ns instructions
  (:use belt.math
        belt.general-utils))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Integer instructions

(defn int-to-bool
  [n]
  (not (zero? n)))

(defn int-dup
  [n]
  (list n n))

(defn int-pop [_])

(defn int-swap
  [n1 n2]
  (list n2 n1))

(defn int-rot
  [n1 n2 n3]
  (list n3 n1 n2))

(defn int-stack-depth
  [& nums]
  (count nums))

(defn int=
  [n1 n2]
  (= n1 n2))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Boolean instructions

(defn rand-bool
  []
  (rand-nth [true false]))

(defn bool-to-int
  [b]
  (if b 1 0))

(defn bool-dup
  [b]
  (list b b))

(defn bool-pop [_])

(defn bool-swap
  [b1 b2]
  (list b2 b1))

(defn bool-rot
  [b1 b2 b3]
  (list b3 b1 b2))

(defn bool-stack-depth
  [& bs]
  (count bs))

(defn bool=
  [b1 b2]
  (= b1 b2))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Character instructions

(defn char-dup
  [c]
  (list c c))

(defn char-pop [_])

(defn char-swap
  [c1 c2]
  (list c2 c1))

(defn char-rot
  [c1 c2 c3]
  (list c3 c1 c2))

(defn char-stack-depth
  [& cs]
  (count cs))

(defn letter?
  [c]
  (boolean ((-> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                vec
                set) c)))

(defn numeral?
  [c]
  (boolean ((-> "0123456789"
                vec
                set) c)))

(defn punctuation?
  [c]
  (boolean ((-> ",;:.?!"
                vec
                set) c)))

(defn char=
  [c1 c2]
  (= c1 c2))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;String instructions

(defn first-string
  [s]
  (first s))

(defn safe-read-string
  [s]
  (try (eval (read-string s))
       (catch Exception e nil)))

(defn string-count
  [s]
  (count s))

(defn string-dup
  [s]
  (list s s))

(defn string-pop [_])

(defn string-swap
  [s1 s2]
  (list s2 s1))

(defn string-rot
  [s1 s2 s3]
  (list s3 s1 s2))

(defn string-stack-depth
  [& strs]
  (count strs))

(defn fn-name?
  [s]
  (fn? (try (eval (read-string s))
            (catch Exception e false))))

(defn string=
  [s1 s2]
  (= s1 s2))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Argument lists for all instructions

(def arglist-map
  '{+ [:int :int]
    - [:int :int]
    * [:int :int]
    pd [:int :int]
    mod [:int :int]
    rand-int [:int]
    int-to-bool [:int]
    int-dup [:int]
    int-pop [:int]
    int-swap [:int :int]
    int-rot [:int :int :int]
    int-stack-depth :int
    pos? [:int]
    neg? [:int]
    zero? [:int]
    int= [:int :int]
    < [:int :int]
    > [:int :int]
    or [:bool :bool]
    and [:bool :bool]
    not [:bool]
    rand-bool []
    bool-to-int [:bool]
    bool-dup [:bool]
    bool-pop [:bool]
    bool-swap [:bool :bool]
    bool-rot [:bool :bool :bool]
    bool-stack-depth :bool
    bool= [:bool :bool]
    char-dup [:char]
    char-pop [:char]
    char-swap [:char :char]
    char-rot [:char :char :char]
    char-stack-depth :char
    letter? [:char]
    numeral? [:char]
    punctuation? [:char]
    char= [:char :char]
    first-string [:string]
    safe-read-string [:string]
    string-count [:string]
    string-dup [:string]
    string-pop [:string]
    string-swap [:string :string]
    string-rot [:string :string :string]
    string-stack-depth :string
    fn-name? [:string]
    string= [:string :string]})
