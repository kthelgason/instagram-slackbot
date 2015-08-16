(ns instagram-slackbot.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [clojail.core :refer [sandbox]]
            [clojail.testers :refer [secure-tester-without-def blanket]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as client]
            [instagram-slackbot.core.instagram :as insta])
  (:import java.io.StringWriter
           java.util.concurrent.TimeoutException)
  (:gen-class))


(def post-url
  (:post-url env))

(def command-token
  (:command-token env))

(defn post-to-slack [s img]
   (let [att [{"title" s "image_url" img}]]
     (client/post post-url
                  {:content-type :json
                   :form-params {:attachments att}})))


(defn handle-subscription [params]
  (if-not (contains? params "hub.challenge")
    {:status 400 :body "Invalid Request"}
    {:status 200 :body (get params "hub.challenge")}))


(defn notification-recieved [body]
  (do
    (let [tag (get (first body) "object_id")]
      (post-to-slack (str "New image for hashtag #" tag)
                     (insta/fetch-image-for-tag tag)))
    {:status 200}))

(defn read-json-body [req]
  (json/read-str (slurp (:body req))))

(defroutes approutes
  (GET "/handle-subscription" req (handle-subscription (:params req)))
  (POST "/handle-subscription" req (notification-recieved (read-json-body req)))
  (route/not-found "Not Found"))

(def app (wrap-defaults approutes
                        api-defaults))

(defn -main [& args]
  (run-jetty app {:port (Integer/parseInt (or (:port env)
                                              "3000"))}))
