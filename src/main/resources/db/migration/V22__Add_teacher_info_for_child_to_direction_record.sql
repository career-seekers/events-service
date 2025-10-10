ALTER TABLE child_to_direction
    ADD institution VARCHAR(255);

ALTER TABLE child_to_direction
    ADD post VARCHAR(255);

ALTER TABLE child_to_direction
    ADD teacher VARCHAR(255);

ALTER TABLE child_to_direction
    ALTER COLUMN direction_id SET NOT NULL;