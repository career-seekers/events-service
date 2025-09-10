ALTER TABLE platforms
    ADD email VARCHAR(255);

ALTER TABLE platforms
    ADD website VARCHAR(255);

ALTER TABLE platforms
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE platforms
    ADD CONSTRAINT uc_platforms_email UNIQUE (email);

ALTER TABLE platforms
    ADD CONSTRAINT uc_platforms_userid UNIQUE (user_id);