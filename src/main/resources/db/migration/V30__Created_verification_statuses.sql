ALTER TABLE events
    ADD verification_status SMALLINT;

UPDATE events
SET verification_status = CASE
                              WHEN verified = true THEN 0
                              WHEN verified = false THEN 1
                              ELSE 2
    END;

ALTER TABLE events
    ALTER COLUMN verification_status SET NOT NULL;

ALTER TABLE events
    ALTER COLUMN verification_status SET DEFAULT 2;