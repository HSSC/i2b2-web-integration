(defproject org.healthsciencessc.i2b2/user-management "0.1.0-SNAPSHOT"
  :description "Plugin to require a user to sign off on a data usage agreement every time they log into the system."
  
  :url "https://github.com/HSSC/i2b2-webclient"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :source-paths ["src/main/clojure"]
  :resource-paths ["src/main/resource"]
  :test-paths ["src/test/clojure"]
  
  ;; Keep java source and project definition out of the artifact
  :jar-exclusions [#"^\." #"^*\/\." #"\.java$" #"project\.clj"]
  
  :plugins [[lein-package "2.0.0"]]
  
  :hooks [leiningen.package.hooks.deploy 
          leiningen.package.hooks.install]
  
  :package {:autobuild true :reuse true}
  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [pliant/configure "0.1.1"]
                 [pliant/process "0.1.0"]
                 [sandbar "0.4.0-SNAPSHOT"]])
