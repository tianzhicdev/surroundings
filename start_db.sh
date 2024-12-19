#!/bin/bash

# Install PostgreSQL using Homebrew
brew install postgresql

# Start PostgreSQL service
brew services start postgresql

# Wait for PostgreSQL to start
sleep 5

# Create a new PostgreSQL database called 'bitcoin'
createdb bitcoin

# Connect to the 'bitcoin' database and create the 'transaction' table
psql -d bitcoin -c "
CREATE TABLE transaction (
    from_address VARCHAR(255),
    to_address VARCHAR(255),
    amount NUMERIC,
    block_number INTEGER,
    timestamp TIMESTAMP
);
"

echo "Database 'bitcoin' and table 'transaction' created successfully."

