ALTER TABLE child_to_direction
    ADD created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();

ALTER TABLE child_to_direction
    ALTER COLUMN created_at SET NOT NULL;