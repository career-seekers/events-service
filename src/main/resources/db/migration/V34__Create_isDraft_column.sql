ALTER TABLE events
    ADD is_draft BOOLEAN DEFAULT false;

ALTER TABLE events
    ALTER COLUMN is_draft SET NOT NULL;