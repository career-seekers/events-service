ALTER TABLE direction_documents
    ADD verified BOOLEAN DEFAULT FALSE;

ALTER TABLE direction_documents
    ALTER COLUMN verified SET NOT NULL;