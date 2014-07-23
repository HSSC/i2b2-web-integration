(ns org.healthsciencessc.i2b2.webclient.pm
  (:require [clojure.java.jdbc :as jdbc]
            [org.healthsciencessc.i2b2.webclient.config :as config]))


(defn to-hash
  "Uses MD5 to hash a value.  This is used to hash passwords prior to being inserted into the database."
  [^String value]
  (apply str
         (map (partial format "%x")
              (.digest (doto
                         (java.security.MessageDigest/getInstance "MD5")
                         .reset
                         (.update (.getBytes value)))))))

;; The database connection definition for the Project Management database.  By default,
;; the datastore configured on JBoss for the PM module is used, but have givent the
;; ability to set the name of a different datastore to use in the WCI properties
;; file.
(def pm-db {:name (config/lookup :pm.database.name "java:/PMBootStrapDS")})

(def sql-select-users
  "SELECT USER_ID as \"user-id\",
          FULL_NAME as \"full-name\",
          PASSWORD as \"password\",
          EMAIL as \"email\",
          STATUS_CD as \"status\",
          CHANGE_DATE as \"change-date\",
          ENTRY_DATE as \"entry-date\",
          CHANGEBY_CHAR as \"change-by\"
     FROM PM_USER_DATA ")

(def sql-select-roles
  "SELECT USER_ID as \"user-id\",
          PROJECT_ID as \"project-id\",
          USER_ROLE_CD as \"role\",
          STATUS_CD as \"status\",
          CHANGE_DATE as \"change-date\",
          ENTRY_DATE as \"entry-date\",
          CHANGEBY_CHAR as \"change-by\"
     FROM PM_PROJECT_USER_ROLES ")

(def sql-select-projects
  "SELECT PROJECT_ID as \"project-id\",
          PROJECT_NAME as \"name\",
          PROJECT_WIKI as \"wiki\",
          PROJECT_KEY as \"key\",
          PROJECT_PATH as \"path\",
          PROJECT_DESCRIPTION as \"description\",
          STATUS_CD as \"status\"
          CHANGE_DATE as \"change-date\"
          ENTRY_DATE as \"entry-date\"
          CHANGEBY_CHAR as \"change-by\"
     FROM PM_PROJECT_DATA ")

(defn users
  "Gets all of the users."
  []
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [sql-select-users]
      (seq rs))))

(defn user
  "Gets a user by it's ID."
  [id]
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [(str sql-select-users " WHERE LOWER(USER_ID)=?") (clojure.string/lower-case id)]
      (first rs))))

(defn projects
  "Gets all of the projects."
  []
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [sql-select-projects]
      (seq rs))))

(defn project
  "Gets a project by it's ID."
  [id]
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [(str sql-select-projects " WHERE PROJECT_ID=?") id]
      (first rs))))

(defn roles
  "Gets the roles for a user."
  []
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [sql-select-roles]
      (seq rs))))

(defn user-roles
  "Gets the roles for a user."
  [user-id]
  (jdbc/with-connection pm-db
    (jdbc/with-query-results rs [(str sql-select-roles " WHERE USER_ID=?") user-id]
      (seq rs))))

(defn change-password
  [user-id password]
  (jdbc/with-connection pm-db
    (jdbc/transaction
      (jdbc/update-values :PM_USER_DATA
                          ["USER_ID=?" user-id]
                          {:PASSWORD (to-hash password)}))))

(defn add-user
  "Adds a user to I2B2"
  [user]
  (jdbc/with-connection pm-db
    (jdbc/transaction
      (jdbc/update-values :PM_USER_DATA
                          [:USER_ID :FULL_NAME :PASSWORD :EMAIL
                           :STATUS_CD :CHANGE_DATE :ENTRY_DATE :CHANGEBY_CHAR]
                          [(:user-id user) (:full-name user) (:password user) (:email user)
                           (:status user) (:change-date user) (:entry-date user) (:change-by user)]))))

(defn add-user-roles
  "Adds a user to I2B2"
  [& roles]
  (jdbc/with-connection pm-db
    (jdbc/transaction
      (apply jdbc/update-values :PM_PROJECT_USER_ROLES
                          [:USER_ID :PROJECT_ID :USER_ROLE_CD
                           :STATUS_CD :CHANGE_DATE :ENTRY_DATE :CHANGEBY_CHAR]
                          (map (fn [role]
                                 [(:user-id role) (:project-id role) (:role role)
                                  (:status role) (:change-date role) (:entry-date role) (:change-by role)]) roles)))))



(comment "I2B2 Table Schemas"
  "Table: PM_APPROVALS
  Columns:
     APPROVAL_ID, APPROVAL_NAME, APPROVAL_DESCRIPTION, APPROVAL_ACTIVATION_DATE, APPROVAL_DEACTIVATION_DATE, OBJECT_CD,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_APPROVALS_PARAMS
  Columns: ID,
     APPROVAL_ID, PARAM_NAME_CD, VALUE, ACTIVATION_DATE, DEACTIVATION_DATE, DATATYPE_CD, OBJECT_CD,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_CELL_DATA
  Columns: CELL_ID, PROJECT_PATH,
     NAME, METHOD_CD, URL, CAN_OVERRIDE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_CELL_PARAMS
  Columns: ID,
     DATATYPE_CD, CELL_ID, PROJECT_PATH, PARAM_NAME_CD, VALUE, CAN_OVERRIDE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_GLOBAL_PARAMS
  Columns: ID,
     DATATYPE_CD, PROJECT_PATH, PARAM_NAME_CD, VALUE, CAN_OVERRIDE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_HIVE_DATA
  Columns: DOMAIN_ID,
     HELP_URL, DOMAIN_NAME, ENVIRONMENT_CD, ACTIVE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_HIVE_PARAMS
  Columns: ID,
     DATATYPE_CD, DOMAIN_ID, PARAM_NAME_CD, VALUE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_PROJECT_PARAMS
  Columns: ID,
     DATATYPE_CD, PROJECT_ID, PARAM_NAME_CD, VALUE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_PROJECT_REQUEST
  Columns: ID,
     TITLE, REQUEST_XML, PROJECT_ID, SUBMIT_CHAR,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_PROJECT_USER_PARAMS
  Columns: ID,
     DATATYPE_CD, PROJECT_ID, USER_ID, PARAM_NAME_CD, VALUE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

     Possible USER_ROLE_CD Values
			  ADMIN
			  DATA_AGG
			  DATA_DEID
			  DATA_LDS
			  DATA_OBFSC
			  EDITOR
			  MANAGER
			  USER

  Table: PM_ROLE_REQUIREMENT
  Columns: TABLE_CD, COLUMN_CD, READ_HIVEMGMT_CD, WRITE_HIVEMGMT_CD,
     NAME_CHAR,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR


  Table: PM_USER_PARAMS
  Columns: ID,
     DATATYPE_CD, USER_ID, PARAM_NAME_CD, VALUE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR

  Table: PM_USER_SESSION
  Columns: USER_ID, SESSION_ID,
     EXPIRED_DATE,
     CHANGE_DATE, STATUS_CD, ENTRY_DATE, CHANGEBY_CHAR
  ")
