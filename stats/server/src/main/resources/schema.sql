CREATE TABLE IF NOT EXISTS endpoint_hits (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR NOT NULL,
    time_stamp DATE NOT NULL
);