ALTER TABLE child_to_direction
    ADD teacher_name VARCHAR(255);

ALTER TABLE child_to_direction
    DROP COLUMN teacher;