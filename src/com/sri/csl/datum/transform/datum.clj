(ns com.sri.csl.datum.transform.datum
  (:require [com.sri.csl.datum.transform
             [misc :as misc]
             [protein :as protein]
             [assay :as assay]
             [treatment :as treatment]
             [environment :as environment]
             [extra :as extra]
             [common :as c]]
            [clojure.pprint :refer [pprint]]
            [instaparse.core :as insta]))

(defn singleton? [line] (not (or (:extra line) (:comment line))))

(defn transformers []
  (merge {:cfirstline (c/simple-merge)
          :sfirstline (c/simple-merge)
          :subject (c/named-merge :subject)
          :changetype (c/component :treatment_type)}
         protein/transformers
         assay/transformers
         treatment/transformers
         environment/transformers
         extra/transformers
         misc/transformers
         ))

(defn datum [ast]
  (let [transformed-ast (insta/transform (transformers) ast)
        lines (rest transformed-ast)]
    (apply merge
           {:extras (filterv :extra lines)
            :comments (mapv :comment (filter :comment lines))}
           (filter singleton? lines))))

