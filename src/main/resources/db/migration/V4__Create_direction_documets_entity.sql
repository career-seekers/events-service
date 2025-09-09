CREATE SEQUENCE IF NOT EXISTS direction_documents_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE direction_documents
(
    id            BIGINT   NOT NULL,
    document_type SMALLINT NOT NULL,
    document_id   BIGINT   NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    user_id       BIGINT,
    direction_id  BIGINT,
    CONSTRAINT pk_direction_documents PRIMARY KEY (id)
);

ALTER TABLE direction_documents
    ADD CONSTRAINT FK_DIRECTION_DOCUMENTS_ON_DIRECTION FOREIGN KEY (direction_id) REFERENCES directions (id);