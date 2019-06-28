ALTER TABLE MAILARCHIVE_MESSAGE ADD COLUMN (
       SUBJECT           VARCHAR (255) NULL,
       BODY              LONGTEXT NULL
);

CREATE INDEX IE_MAILARC_SUBJECT ON MAILARCHIVE_MESSAGE
(
       SUBJECT                   ASC
);

