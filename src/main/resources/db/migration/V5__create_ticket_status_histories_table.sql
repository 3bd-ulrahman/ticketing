CREATE TABLE ticket_status_histories (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_by BIGINT NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ticket_status_histories_ticket_id_foreign FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT ticket_status_histories_changed_by_foreign FOREIGN KEY (changed_by) REFERENCES users(id)
);

CREATE INDEX ticket_status_histories_ticket_id_index ON ticket_status_histories(ticket_id);
