CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    assigned_to BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT tickets_category_id_foreign FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT tickets_created_by_foreign FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT tickets_assigned_to_foreign FOREIGN KEY (assigned_to) REFERENCES users(id)
);

CREATE INDEX tickets_status_index ON tickets(status);
CREATE INDEX tickets_priority_index ON tickets(priority);
CREATE INDEX tickets_category_id_index ON tickets(category_id);
CREATE INDEX tickets_created_by_index ON tickets(created_by);
CREATE INDEX tickets_assigned_to_index ON tickets(assigned_to);
