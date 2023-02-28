-- :name create-ingredient! :! :n
-- :doc creates a new ingredient
INSERT INTO ingredients
(name, amount)
VALUES (:name, :amount)

-- :name get-ingredients :? :*
-- :doc selects all available ingredients
SELECT * FROM ingredients