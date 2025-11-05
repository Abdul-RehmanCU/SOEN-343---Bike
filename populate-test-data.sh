#!/bin/bash

echo "🚴 Populating QwikRide with test data..."
echo "=========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to make API calls with error handling
make_api_call() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "${BLUE}📡 $description${NC}"
    
    if [ -n "$data" ]; then
        response=$(curl -s -X $method "$url" \
            -H "Content-Type: application/json" \
            -d "$data" \
            -w "\n%{http_code}")
    else
        response=$(curl -s -X $method "$url" \
            -w "\n%{http_code}")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✅ Success (HTTP $http_code)${NC}"
        echo "$body" | jq . 2>/dev/null || echo "$body"
    else
        echo -e "${RED}❌ Failed (HTTP $http_code)${NC}"
        echo "$body"
    fi
    echo ""
}

# 1. Create Bike Stations
echo -e "${YELLOW}🏗️  Step 1: Creating Bike Stations${NC}"
echo "----------------------------------------"

# Station 1 - Downtown Central
make_api_call "POST" "http://localhost:8080/api/stations" \
'{
    "name": "Downtown Central",
    "address": "123 Main Street, Downtown",
    "capacity": 25,
    "currentBikeCount": 0,
    "status": "ACTIVE"
}' "Creating Downtown Central Station"

# Station 2 - University Campus
make_api_call "POST" "http://localhost:8080/api/stations" \
'{
    "name": "University Campus",
    "address": "456 University Ave, Campus District",
    "capacity": 30,
    "currentBikeCount": 0,
    "status": "ACTIVE"
}' "Creating University Campus Station"

# Station 3 - Shopping Mall
make_api_call "POST" "http://localhost:8080/api/stations" \
'{
    "name": "Shopping Mall",
    "address": "789 Commerce Blvd, Shopping District",
    "capacity": 20,
    "currentBikeCount": 0,
    "status": "ACTIVE"
}' "Creating Shopping Mall Station"

# Station 4 - Park & Ride
make_api_call "POST" "http://localhost:8080/api/stations" \
'{
    "name": "Park & Ride",
    "address": "321 Transit Way, Suburb",
    "capacity": 15,
    "currentBikeCount": 0,
    "status": "ACTIVE"
}' "Creating Park & Ride Station"

# 2. Create Bikes
echo -e "${YELLOW}🚲 Step 2: Creating Bikes${NC}"
echo "----------------------------"

# Create Standard Bikes for Station 1 (Downtown Central)
for i in {1..8}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"STANDARD\",
        \"stationId\": 1,
        \"serialNumber\": \"STD-DT-00$i\"
    }" "Creating Standard Bike STD-DT-00$i at Downtown Central"
done

# Create E-Bikes for Station 1 (Downtown Central)
for i in {1..5}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"E_BIKE\",
        \"stationId\": 1,
        \"serialNumber\": \"EBIKE-DT-00$i\"
    }" "Creating E-Bike EBIKE-DT-00$i at Downtown Central"
done

# Create Standard Bikes for Station 2 (University Campus)
for i in {1..10}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"STANDARD\",
        \"stationId\": 2,
        \"serialNumber\": \"STD-UC-00$i\"
    }" "Creating Standard Bike STD-UC-00$i at University Campus"
done

# Create E-Bikes for Station 2 (University Campus)
for i in {1..7}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"E_BIKE\",
        \"stationId\": 2,
        \"serialNumber\": \"EBIKE-UC-00$i\"
    }" "Creating E-Bike EBIKE-UC-00$i at University Campus"
done

# Create Standard Bikes for Station 3 (Shopping Mall)
for i in {1..6}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"STANDARD\",
        \"stationId\": 3,
        \"serialNumber\": \"STD-SM-00$i\"
    }" "Creating Standard Bike STD-SM-00$i at Shopping Mall"
done

# Create E-Bikes for Station 3 (Shopping Mall)
for i in {1..4}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"E_BIKE\",
        \"stationId\": 3,
        \"serialNumber\": \"EBIKE-SM-00$i\"
    }" "Creating E-Bike EBIKE-SM-00$i at Shopping Mall"
done

# Create Standard Bikes for Station 4 (Park & Ride)
for i in {1..5}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"STANDARD\",
        \"stationId\": 4,
        \"serialNumber\": \"STD-PR-00$i\"
    }" "Creating Standard Bike STD-PR-00$i at Park & Ride"
done

# Create E-Bikes for Station 4 (Park & Ride)
for i in {1..3}; do
    make_api_call "POST" "http://localhost:8080/api/bikes/create" \
    "{
        \"type\": \"E_BIKE\",
        \"stationId\": 4,
        \"serialNumber\": \"EBIKE-PR-00$i\"
    }" "Creating E-Bike EBIKE-PR-00$i at Park & Ride"
done

# 3. Verify Setup
echo -e "${YELLOW}📊 Step 3: Verifying Setup${NC}"
echo "----------------------------"

make_api_call "GET" "http://localhost:8080/api/stations" "" "Getting all stations"
make_api_call "GET" "http://localhost:8080/api/bikes" "" "Getting all bikes"

echo -e "${GREEN}🎉 Test data population complete!${NC}"
echo ""
echo -e "${YELLOW}📋 Summary:${NC}"
echo "• 4 Bike Stations created"
echo "• 29 Standard Bikes created"
echo "• 19 E-Bikes created"
echo "• Total: 48 bikes across all stations"
echo ""
echo -e "${BLUE}🔧 Next Steps:${NC}"
echo "1. Open http://localhost:5173 in your browser"
echo "2. Login as operator (username: operator, password: operator123)"
echo "3. Navigate to Bike Management"
echo "4. Test bike operations:"
echo "   • Reserve bikes as a user"
echo "   • Checkout and return bikes"
echo "   • Move bikes between stations (operator)"
echo "   • View station capacity and bike counts"

