ALTER TABLE direction_documents
    ALTER COLUMN verified DROP NOT NULL;

UPDATE direction_documents
SET verified = NULL
WHERE verified = false;