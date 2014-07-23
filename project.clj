(defproject org.healthsciencessc.i2b2/root "0.1.0-SNAPSHOT"
  :description "Provides an integration layer to the i2b2 web client along with 
                multiple plugins."
  
  :url "https://github.com/HSSC/i2b2-webclient"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :plugins [[lein-package "2.0.0"]
            [lein-sub "0.2.4"]]
  
  :hooks [leiningen.package.hooks.deploy 
          leiningen.package.hooks.install]
  
  :sub ["shibboleth" "usage-agreement" "user-management" "web-integrator"])

