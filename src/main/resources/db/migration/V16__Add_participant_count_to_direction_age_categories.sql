ALTER TABLE direction_age_categories
    ADD current_participants_count BIGINT DEFAULT 0;

ALTER TABLE direction_age_categories
    ADD max_participants_count BIGINT DEFAULT 0;

ALTER TABLE direction_age_categories
    ALTER COLUMN current_participants_count SET NOT NULL;

ALTER TABLE direction_age_categories
    ALTER COLUMN max_participants_count SET NOT NULL;