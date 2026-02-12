-- V2__updated_at_trigger.sql
-- Auto-update updated_at on UPDATE for all tables that have updated_at

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
t TEXT;
BEGIN
    FOREACH t IN ARRAY ARRAY[
        'recipe_category',
        'recipe_difficulty',
        'ingredient',
        'ingredient_unit',
        'tag',
        'utensil',
        'recipe',
        'recipe_ingredient',
        'recipe_step',
        'recipe_tag',
        'recipe_utensil'
    ]
    LOOP
        EXECUTE format('DROP TRIGGER IF EXISTS trg_%s_updated_at ON %I;', t, t);
EXECUTE format(
        'CREATE TRIGGER trg_%s_updated_at
         BEFORE UPDATE ON %I
         FOR EACH ROW
         EXECUTE FUNCTION set_updated_at();',
        t, t
        );
END LOOP;
END $$;
