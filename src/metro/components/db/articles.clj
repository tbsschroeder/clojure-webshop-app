(ns metro.components.db.articles
  (:require [korma.core :as kc]))

(kc/defentity article
  (kc/table :articles)
  (kc/entity-fields :id :title :description :category :image :count))

(defn drop-table! []
  (kc/exec-raw "DROP TABLE IF EXISTS articles"))

(defn clear-table! []
  (kc/delete article))

(defn add! [title description category image count]
  (kc/insert article (kc/values {:title       title
                                 :description description
                                 :category    category
                                 :image       image
                                 :count       count})))

(defn add-with-id! [id title description category image count]
  (kc/insert article (kc/values {:id          id
                                 :title       title
                                 :description description
                                 :category    category
                                 :image       image
                                 :count       count})))

(defn delete! [id]
  (kc/delete article (kc/where {:id id})))

; not working right now
;(defn get-count! [id]
;  (kc/select article (kc/fields :count) (kc/where {:id id})))

(defn inc! [id]
  (kc/exec-raw (format "UPDATE articles SET count = count + 1 WHERE id = %s" id)))

(defn dec! [id]
  (kc/exec-raw (format "UPDATE articles SET count = count - 1 WHERE id = %s" id)))

(defn rem! [id]
  (kc/exec-raw (format "UPDATE articles SET count = 0 WHERE id = %s" id)))

(defn query-all []
  (kc/select article))

(defn query-article [id]
  (kc/select article (kc/where {:id (int id)}))
  ;(first (filter #(= (:id %) (int id)) (query-all)))
  )

(defn query-all-with-count []
  (kc/select article (kc/where (> :count 0))))

(defn has-articles-with-data? []
  (pos? (reduce + (map #(get % :count) (query-all-with-count)))))

(defn has-data? []
  (pos? (count (query-all))))

(defn create-table! [is-testing]
  (drop-table!)
  (if is-testing
    (kc/exec-raw "CREATE TABLE IF NOT EXISTS articles(id INTEGER PRIMARY KEY, title TEXT NOT NULL, description TEXT NOT NULL, category TEXT NOT NULL, image TEXT NOT NULL, count INTEGER DEFAULT 0 NOT NULL)")
    (kc/exec-raw "CREATE TABLE IF NOT EXISTS articles(id SERIAL, title TEXT NOT NULL, description TEXT NOT NULL, category TEXT NOT NULL, image TEXT NOT NULL, count INTEGER DEFAULT 0 NOT NULL)"))
  (add! "Bacon"
        "Bacon is a type of salt-cured pork. Bacon is prepared from several different cuts of meat, typically from the pork belly or from back cuts, which have less fat than the belly. And it's delicious!"
        "Meat"
        "img/products/bacon.jpg"
        0)
  (add! "Crinkle Cut Fries"
        "Crinkle-cutting is slicing that leaves a corrugated surface. This is done with corrugated knives or mandoline blades. Crinkle-cut potato chips are sometimes called ruffled."
        "Potato Products "
        "img/products/crinklefries.jpg"
        0)
  (add! "Deep Frying Fat"
        "Deep fat frying is a cooking method that can be used to cook food. The process involves submerging a food in extremely hot oil until it reaches a safe minimum internal temperature."
        "Oil & Fat"
        "img/products/fryingfat.jpg"
        0)
  (add! "Garlic"
        "Garlic is a species in the onion genus. Its close relatives include the onion, shallot, leek, chive, and chinese onion. Garlic is native to central asia and ..."
        "Vegetables"
        "img/products/garlic.jpg"
        0)
  (add! "Ice Cream"
        "Ice cream is a sweetened frozen food typically eaten as a snack or dessert. It may be made from dairy milk or cream and is flavored with a sweetener and any spice."
        "Sweets"
        "img/products/icecream.jpg"
        0)
  (add! "Olive Oil"
        "Olive oil is a liquid obtained from olives. The oil is produced by pressing whole olives. It is commonly used in cooking, whether for frying or as a salad dressing."
        "Oil & Fat"
        "img/products/oliveoil.jpg"
        0)
  (add! "Shrimps"
        "Shrimp is shrimp and we do not care whether they are black tiger, white tiger or sea tiger. Just cook or grill them with a lot of garlic and, if you like, with a steak!"
        "Fish"
        "img/products/shrimps.jpg"
        0)
  (add! "Steakhouse Fries"
        "Steakhouse fries offer an intense potato experience. Made from whole potatoes extra wide cut, they are especially potato and go perfectly with hearty meat dishes."
        "Potato Products"
        "img/products/steakhousefries.jpg"
        0)
  (add! "Sweet Potato Fries"
        "Fried sweet potato features in a variety of dishes and cuisines including the popular sweet potato fries, a variation of French fries using sweet potato instead of potato."
        "Potato Products"
        "img/products/sweetpotatoefries.jpg"
        0)
  (add! "Tortilla Chips"
        "A tortilla chip is a snack food made from corn tortillas, which are cut into wedges and then fried—or baked. Corn tortillas are made of corn, vegetable oil, salt and water."
        "Snack"
        "img/products/tortilla.jpg"
        0))

(comment
  (create-table! false)
  (add! "deleteme" "deleteit" "deletenow" 0)
  (delete! 10)
  (clear-table!))
