ALTER TABLE directions
    ADD expert_id BIGINT DEFAULT NULL;

ALTER TABLE directions
    ADD participants_count BIGINT DEFAULT 0;