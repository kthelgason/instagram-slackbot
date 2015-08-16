(ns instagram-slackbot.core.instagram
  (:require [environ.core :refer [env]]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(def client-id
  (:client-id env))

(defn url-string-for-tag [tag]
  (str "https://api.instagram.com/v1/tags/" tag "/media/recent?count=1&client_id=" client-id))


(defn fetch-image-for-tag [tag]
  (get (get-in  (first (get (json/read-str
                              (:body (client/get
                                      (url-string-for-tag tag) {:content-type :json})))
                             "data"))
                ["images" "standard_resolution"])
       "url"))

