(defproject org.healthsciencessc.i2b2/web-integrator "0.1.0-SNAPSHOT"
  :description "Provides an integration layer to the i2b2 web client in order to provide additional functionality, such as SSO integration."
  
  :url "https://github.com/HSSC/i2b2-webclient"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :source-paths ["src/main/clojure"]
  :resource-paths ["src/main/resource"]
  :test-paths ["src/test/clojure"]
  
  ;; Keep java source and project definition out of the artifact
  :jar-exclusions [#"^\." #"^*\/\." #"\.java$" #"project\.clj"]
  
  :plugins [[lein-package "2.0.1"]
            [lein-ring "0.8.11"]]
  
  :hooks [leiningen.package.hooks.deploy 
          leiningen.package.hooks.install]
  
  :package {:autobuild true :reuse true :skipjar true
            :artifacts [{:build "ring uberwar" :classifier "standalone" :extension "war"}]}
  
  :ring {:handler org.healthsciencessc.i2b2.webclient.core/app
         :init org.healthsciencessc.i2b2.webclient.core/init
         :destroy org.healthsciencessc.i2b2.webclient.core/destroy
         :port 8081}
  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [pliant/configure "0.1.2"]
                 [pliant/process "0.1.1"]
                 [pliant/webpoint "0.1.0"]
                 [hiccup "1.0.0"]
                 [sandbar "0.4.0-SNAPSHOT"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.clojure/data.json "0.2.2"]]
  
  :profiles {:all {:dependencies [[org.healthsciencessc.i2b2/shibboleth "0.1.0-SNAPSHOT"]
                                  [org.healthsciencessc.i2b2/usage-agreement "0.1.0-SNAPSHOT"]
                                  [org.healthsciencessc.i2b2/user-management "0.1.0-SNAPSHOT"]]}})
