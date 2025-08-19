#!/bin/bash

echo "========================================"
echo "News Aggregator Project"
echo "========================================"
echo ""
echo "Starting the project with Docker..."
echo ""

# Check if Docker is running
if ! docker version >/dev/null 2>&1; then
    echo "ERROR: Docker is not running or not installed!"
    echo "Please start Docker and try again."
    exit 1
fi

echo "Building and starting all services..."
docker-compose up --build

echo ""
echo "========================================"
echo "Project is running!"
echo "========================================"
echo "Frontend: http://localhost:3000"
echo "Backend API: http://localhost:8080"
echo "Admin Dashboard: http://localhost:3000/admin"
echo ""
echo "Default Admin Credentials:"
echo "Email: admin@newsaggregator.com"
echo "Password: admin123"
echo "" 