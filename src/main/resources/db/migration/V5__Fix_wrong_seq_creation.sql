DROP SEQUENCE direction_documents_seq CASCADE;

CREATE SEQUENCE IF NOT EXISTS direction_documents_id_seq;
ALTER TABLE direction_documents
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE direction_documents
    ALTER COLUMN id SET DEFAULT nextval('direction_documents_id_seq');

ALTER SEQUENCE direction_documents_id_seq OWNED BY direction_documents.id;