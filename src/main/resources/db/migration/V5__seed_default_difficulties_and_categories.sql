-- V5__seed_default_difficulties_and_categories.sql
-- Seed default values for recipe_difficulty and recipe_category

-- 1) Ensure "name" is unique so inserts are idempotent
ALTER TABLE recipe_difficulty
    ADD CONSTRAINT uq_recipe_difficulty_name UNIQUE (name);

ALTER TABLE recipe_category
    ADD CONSTRAINT uq_recipe_category_name UNIQUE (name);

-- 2) Insert default difficulties
INSERT INTO recipe_difficulty (name)
VALUES
    ('Facile'),
    ('Moyen'),
    ('Difficile')
    ON CONFLICT (name) DO NOTHING;

-- 3) Insert default categories
INSERT INTO recipe_category (name)
VALUES
    ('Entrée'),
    ('Plat principal'),
    ('Dessert'),
    ('Accompagnement'),
    ('Boisson')
    ON CONFLICT (name) DO NOTHING;
