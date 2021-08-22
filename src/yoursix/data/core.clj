(ns yoursix.data.core
  (:require [clojure.test :refer [deftest is testing use-fixtures]]))

(def store (atom {}))

(defn- gen-id []
  (-> (java.util.UUID/randomUUID)
      (str)))

(defn fetch [collection]
  (-> @store
      (get collection)
      (or [])))

(defn fetch-by-id [collection id]
  (->> collection
       fetch
       (filter #(= id (:id %)))
       (first)))

(defn insert! [collection entry]
  (let [id (-> entry :id (or (gen-id)))
        next-entry (assoc entry :id id)
        current-store @store
        next-collection (-> current-store
                            (get collection)
                            (or [])
                            (conj next-entry))]
    (swap! store assoc collection next-collection)
    next-entry))

(use-fixtures :each (fn [f]
                      (swap! store (fn [_] {}))
                      (f)))

(deftest insert!-entry
  (testing "should add entry to data store"
    (let [entry {:id "0cf99e67-16e0-4fd8-a081-dc14a8c80e53"
                 :name "Michael Scott"
                 :role "Regional manager"}
          expected {:employees [{:id "0cf99e67-16e0-4fd8-a081-dc14a8c80e53"
                                 :name "Michael Scott"
                                 :role "Regional manager"}]}]

      (insert! :employees entry)
      (is (= expected @store)))))

(deftest insert!-assign-id
  (testing "should assign id if not included in entry"
    (let [entry {:name "Michael Scott"
                 :role "Regional manager"}
          expected {:employees [{:id "3a1f30ca-2869-4742-962d-1b4b7c35cd76"
                                 :name "Michael Scott"
                                 :role "Regional manager"}]}]

      (with-redefs [gen-id (constantly "3a1f30ca-2869-4742-962d-1b4b7c35cd76")]
        (insert! :employees entry))
      (is (= expected @store)))))

(deftest fetch-test
  (testing "should return list if collection is unset"
    (is (= [] (fetch :employees))))

  (testing "should return all entries for the collection"
    (let [entries [{:id "3a1f30ca-2869-4742-962d-1b4b7c35cd76"
                    :name "Michael Scott"
                    :role "Regional manager"}
                   {:id "467932bc-3a30-4e06-b4bf-71fadde1a5d3"
                    :name "Dwight Schrute"
                    :role "Assistant [to the] Regional manager"}]]
      (insert! :employees (first entries))
      (insert! :employees (last entries))
      (is (= entries (fetch :employees))))))

(deftest fetch-by-id-test
  (testing "should return all entries for the collection"
    (let [entries [{:id "3a1f30ca-2869-4742-962d-1b4b7c35cd76"
                    :name "Michael Scott"
                    :role "Regional manager"}
                   {:id "467932bc-3a30-4e06-b4bf-71fadde1a5d3"
                    :name "Dwight Schrute"
                    :role "Assistant [to the] Regional manager"}]

          expected (last entries)]
      (insert! :employees (first entries))
      (insert! :employees (last entries))
      (is (= expected (fetch-by-id :employees "467932bc-3a30-4e06-b4bf-71fadde1a5d3")))))

  (testing "should return nil if entry does not exist"
    (is (nil? (fetch-by-id :employees "ab1574ac-fc86-4699-b1a1-22419d3ab8fc")))))
