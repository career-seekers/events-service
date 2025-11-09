ALTER TABLE events
    ADD created_at TIMESTAMP WITHOUT TIME ZONE default now();

ALTER TABLE events
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();

ALTER TABLE events
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE events
    ALTER COLUMN updated_at SET NOT NULL;