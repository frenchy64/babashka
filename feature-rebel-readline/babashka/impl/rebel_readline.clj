(ns babashka.impl.rebel-readline
  {:no-doc true}
  (:require rebel-readline.main
            [sci.core :as sci]))

(def cns (sci/create-ns 'rebel-readline.main nil))

(def rebel-readline-main-namespace
  {'-main (sci/copy-var rebel-readline.main/-main cns)})
