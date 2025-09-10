ALTER TABLE platforms
    ADD CONSTRAINT uc_platforms_userid UNIQUE (user_id);