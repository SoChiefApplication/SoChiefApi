-- V3__recipe_step_position.sql

-- 1) Ajouter la colonne position
ALTER TABLE recipe_step
    ADD COLUMN position INTEGER;

-- 2) Copier l'existant (step_index) vers position
UPDATE recipe_step
SET position = step_index
WHERE position IS NULL;

-- 3) Mettre NOT NULL maintenant que c'est rempli
ALTER TABLE recipe_step
    ALTER COLUMN position SET NOT NULL;

-- 4) Unicité (une position unique par recette)
ALTER TABLE recipe_step
    ADD CONSTRAINT uq_recipe_step_recipe_position UNIQUE (recipe_id, position);

-- 5) (Optionnel) supprimer step_index si tu ne veux plus l'avoir
ALTER TABLE recipe_step
DROP COLUMN step_index;

-- 6) Index utile
CREATE INDEX IF NOT EXISTS ix_recipe_step_recipe_position
    ON recipe_step (recipe_id, position);
