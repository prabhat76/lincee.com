#!/bin/bash

echo "=========================================="
echo "Starting Lincee Backend Application"
echo "=========================================="
echo ""

# Kill any existing processes on port 8080
echo "Checking for existing processes on port 8080..."
lsof -ti:8080 | xargs kill -9 2>/dev/null || true
echo "✓ Port 8080 is clear"
echo ""

# Clean and rebuild
echo "Building application..."
cd "$(dirname "$0")"
mvn clean package -DskipTests -q
if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi
echo "✓ Build successful"
echo ""

# Start the application with verbose output
echo "Starting application..."
echo "Watch for these key lines:"
echo "  - 'Processing PersistenceUnitInfo'"
echo "  - 'HHH000228: Schema update complete'"  
echo "  - 'Started LinceeApplication'"
echo ""
echo "=========================================="
echo ""

java -jar target/lincee-backend-1.0.0.jar 2>&1 | tee application.log

echo ""
echo "Application stopped. Check application.log for details."
