-- V4__pg_trgm_indices.sql
-- Activer pg_trgm et ajouter des index GIN pour améliorer les recherches LIKE

CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Index sur recipe.title
CREATE INDEX IF NOT EXISTS ix_recipe_title_trgm
    ON recipe USING GIN (lower(title) gin_trgm_ops);

-- Index sur ingredient.name
CREATE INDEX IF NOT EXISTS ix_ingredient_name_trgm
    ON ingredient USING GIN (lower(name) gin_trgm_ops);

-- Index sur tag.name
CREATE INDEX IF NOT EXISTS ix_tag_name_trgm
    ON tag USING GIN (lower(name) gin_trgm_ops);
