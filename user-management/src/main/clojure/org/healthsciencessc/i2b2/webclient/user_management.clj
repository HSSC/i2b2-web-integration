(ns org.healthsciencessc.i2b2.webclient.user-management
  (:require [org.healthsciencessc.i2b2.webclient.process.unauthorized :as unauthorized]
            [org.healthsciencessc.i2b2.webclient.ui.action :as action]
            [org.healthsciencessc.i2b2.webclient.ui.form :as form]
            [org.healthsciencessc.i2b2.webclient.ui.layout :as layout]
            [org.healthsciencessc.i2b2.webclient.auth :as auth]
            [org.healthsciencessc.i2b2.webclient.respond :as respond]
            [org.healthsciencessc.i2b2.webclient.text :as text]
            [sandbar.stateful-session :as sandbar]
            [pliant.webpoint.request :as endpoint]
            [pliant.process :refer [defprocess deflayer as-method continue]]))


(def fields [{:name :user-id :type :hidden}
             {:name :first-name :type :text :label (text/text :plugin.user-registration.firstname.label) :contain true}
             {:name :last-name :type :text :label (text/text :plugin.user-registration.lastname.label) :contain true}
             {:name :title :type :select-one :label (text/text :plugin.user-registration.title.label) :contain true
              :items [{:value "" :label ""}
                      {:value "Mr" :label "Mr"}
                      {:value "Mrs" :label "Mrs"}
                      {:value "Dr" :label "Dr"}
                      {:value "Prof" :label "Prof"}]}
             {:name :email :type :text :label (text/text :plugin.user-registration.email.label) :contain true}
             {:name :telephone :type :text :label (text/text :plugin.user-registration.telephone.label) :contain true}])

;; Provides a way for users to request access to the i2b2 system if they are currently not 
;; a user.
(deflayer unauthorized/view-unauthorized view-unauthorized-user-registration
  [request]
  (if-let [user (auth/get-user)]
    (layout/render-page request {:title (text/text :plugin.user-registration.title)}
                        (form/form {:url "/api/user/registration"
                                    :method :post}
                                   (form/render-fields {} fields user)))
    (continue)))

(defprocess api-user-registration
  [request]
  
  )

(as-method api-user-registration endpoint/endpoints "post-api-user-registration")

