(ns instagram-slackbot.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [clojail.core :refer [sandbox]]
            [clojail.testers :refer [secure-tester-without-def blanket]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as client])
  (:import java.io.StringWriter
           java.util.concurrent.TimeoutException)
  (:gen-class))


(def post-url
  (:post-url env))

(def command-token
  (:command-token env))

(defn post-to-slack
  ([s channel]
     (let [p (if channel {:channel channel} {})]
       (client/post post-url
                   {:content-type :json
                    :form-params (assoc p :text s)})))
  ([s]
     (post-to-slack s nil)))


(defn handle-subscription [params]
  (if-not (contains?  params "hub.challenge")
    {:status 400 :body "Invalid Request"}
    {:status 200 :body (get params "hub.challenge")}))

(defn notification-recieved [body]
  (do
    (post-to-slack (str "New image for hashtag #" (get (first body) "object_id")))
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
