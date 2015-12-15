(ns asyncxmascard.core
  (:require [clojure.core.async
             :as a
             :refer [close! >! <! >!! <!! go go-loop chan timeout]]))

(defn new-card
  [from to subject message]
  {:from from
   :to to
   :subject subject
   :message message})

(def card-ch (chan))

(def friends [{:name "Adrian" :email "adrian@email.com"}
              {:name "Leo" :email "leo@dicaprio.com"}
              {:name "Tom" :email "tom@m.com"}
              {:name "Simon" :email "simon@example.com"}
              {:name "Boris" :email "contact@boris.com"}
              {:name "Donald" :email "donald@duck.com"}])

(defn new-message
  [friend]
  (str "Hello " (:name friend) ", Have a merry little christmas!"))

(defn spam
  [friends]
  (doseq [f friends]
    (>!! card-ch (new-card "hello@yourbestfriend.co.uk" (:email f) "hohoho" (new-message f)))))

(defn postman
  [postman-name]
  (go-loop [count 0]
    (<! (timeout (rand-int 5000)))
    (if-let [card (<! card-ch)]
      (do (println "POSTMAN: " postman-name count)
          (println card)
          (recur (inc count))))))

(postman "Pat")
(postman "Bob")

(spam friends)
