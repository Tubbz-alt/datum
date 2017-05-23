(ns com.sri.csl.datum.parse
  (:require [instaparse.core :as insta]
            [com.sri.csl.datum.transform.datum :as datum]))

(insta/defparser parser (slurp (clojure.java.io/resource "grammar.bnf")))


(defn parse-datum [datum]
  (let [ast (parser (:text datum))
        result {:meta datum}]
    (if (insta/failure? ast)
      (assoc result :parse-error ast)
      (try
        (merge result (datum/datum ast))
        (catch Exception e
          (assoc result :transform-error e))))))
