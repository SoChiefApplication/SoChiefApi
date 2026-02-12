-- V1__init_schema.sql
-- PostgreSQL schema for SoChief recipes domain

-- ============
-- Base tables
-- ============

CREATE TABLE recipe_category
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE recipe_difficulty
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE ingredient
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE ingredient_unit
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE tag
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    CONSTRAINT uq_tag_name UNIQUE (name)
);

CREATE TABLE utensil
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    CONSTRAINT uq_utensil_name UNIQUE (name)
);

CREATE TABLE recipe
(
    id               BIGSERIAL PRIMARY KEY,
    title            TEXT        NOT NULL,
    description      TEXT,
    category_id      BIGINT      NOT NULL,
    difficulty_id    BIGINT      NOT NULL,
    initial_portions INTEGER     NOT NULL,
    preparation_time BIGINT      NOT NULL,
    cooking_time     BIGINT      NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ,

    CONSTRAINT fk_recipe_category
        FOREIGN KEY (category_id) REFERENCES recipe_category (id),

    CONSTRAINT fk_recipe_difficulty
        FOREIGN KEY (difficulty_id) REFERENCES recipe_difficulty (id)
);

-- ======================
-- Association / children
-- ======================

CREATE TABLE recipe_ingredient
(
    id            BIGSERIAL PRIMARY KEY,
    recipe_id     BIGINT      NOT NULL,
    ingredient_id BIGINT      NOT NULL,
    quantity      BIGINT      NOT NULL,
    unit_id       BIGINT      NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ,

    CONSTRAINT fk_recipe_ingredient_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE,

    CONSTRAINT fk_recipe_ingredient_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),

    CONSTRAINT fk_recipe_ingredient_unit
        FOREIGN KEY (unit_id) REFERENCES ingredient_unit (id),

    CONSTRAINT uq_recipe_ingredient UNIQUE (recipe_id, ingredient_id),

    CONSTRAINT ck_recipe_ingredient_quantity_positive CHECK (quantity > 0)
);

CREATE TABLE recipe_step
(
    id          BIGSERIAL PRIMARY KEY,
    recipe_id   BIGINT      NOT NULL,
    -- required by @OrderColumn(name = "step_index") on Recipe.steps
    step_index  INTEGER     NOT NULL,
    description TEXT,
    duration    BIGINT      NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ,

    CONSTRAINT fk_recipe_step_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE,

    CONSTRAINT ck_recipe_step_duration_positive CHECK (duration > 0)
);

CREATE TABLE recipe_tag
(
    id         BIGSERIAL PRIMARY KEY,
    recipe_id  BIGINT      NOT NULL,
    tag_id     BIGINT      NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_recipe_tag_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE,

    CONSTRAINT fk_recipe_tag_tag
        FOREIGN KEY (tag_id) REFERENCES tag (id),

    CONSTRAINT uq_recipe_tag UNIQUE (recipe_id, tag_id)
);

CREATE TABLE recipe_utensil
(
    id         BIGSERIAL PRIMARY KEY,
    recipe_id  BIGINT      NOT NULL,
    utensil_id BIGINT      NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_recipe_utensil_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE,

    CONSTRAINT fk_recipe_utensil_utensil
        FOREIGN KEY (utensil_id) REFERENCES utensil (id),

    CONSTRAINT uq_recipe_utensil UNIQUE (recipe_id, utensil_id)
);

-- =========
-- Indexes
-- =========

CREATE INDEX ix_recipe_category_id ON recipe (category_id);
CREATE INDEX ix_recipe_difficulty_id ON recipe (difficulty_id);

CREATE INDEX ix_recipe_ingredient_recipe_id ON recipe_ingredient (recipe_id);
CREATE INDEX ix_recipe_ingredient_ingredient_id ON recipe_ingredient (ingredient_id);
CREATE INDEX ix_recipe_ingredient_unit_id ON recipe_ingredient (unit_id);

CREATE INDEX ix_recipe_step_recipe_id ON recipe_step (recipe_id);
CREATE INDEX ix_recipe_step_recipe_step_index ON recipe_step (recipe_id, step_index);

CREATE INDEX ix_recipe_tag_recipe_id ON recipe_tag (recipe_id);
CREATE INDEX ix_recipe_tag_tag_id ON recipe_tag (tag_id);

CREATE INDEX ix_recipe_utensil_recipe_id ON recipe_utensil (recipe_id);
CREATE INDEX ix_recipe_utensil_utensil_id ON recipe_utensil (utensil_id);
