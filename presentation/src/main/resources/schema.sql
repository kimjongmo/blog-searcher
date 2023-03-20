DROP TABLE IF EXISTS blog_search_record;

CREATE TABLE blog_search_record
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    vendor    VARCHAR(20)  NOT NULL,
    keyword   VARCHAR(100) NOT NULL,
    search_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_keyword_search_at ON blog_search_record (keyword);