#!/bin/bash
# Database backup script
# Run this after `docker compose up` is running

set -e

BACKUP_FILE="backup.sql"

echo "Creating database backup..."
docker compose exec -T postgres pg_dump -U postgres ticketing > "$BACKUP_FILE"
echo "Backup saved to $BACKUP_FILE"
echo "File size: $(wc -c < "$BACKUP_FILE") bytes"
