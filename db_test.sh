#!/bin/bash

# Connect to the 'bitcoin' database and execute a query to count rows in the 'transaction' table
psql -d bitcoin -c "SELECT COUNT(*) FROM transaction;"


