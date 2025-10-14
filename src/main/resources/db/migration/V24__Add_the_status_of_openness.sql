ALTER TABLE direction_age_categories
    ADD is_disabled BOOLEAN DEFAULT FALSE;

ALTER TABLE direction_age_categories
    ALTER COLUMN is_disabled SET NOT NULL;