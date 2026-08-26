CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT comments_ticket_id_foreign FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT comments_user_id_foreign FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX comments_ticket_id_index ON comments(ticket_id);
