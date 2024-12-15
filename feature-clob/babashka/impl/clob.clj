(ns babashka.impl.clob
  {:no-doc true}
  (:require clob.frontend.rebel
            [sci.core :as sci]))

(def cns (sci/create-ns 'clob.frontend.rebel nil))

(def clob-frontend-rebel-namespace
  {'-main (sci/copy-var clob.frontend.rebel/-main cns)})
