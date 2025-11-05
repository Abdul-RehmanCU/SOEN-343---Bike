#!/bin/bash

echo "🚴 QwikRide Bike Management Operations Test"
echo "==========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Get authentication token
echo -e "${BLUE}🔐 Getting operator authentication token...${NC}"
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"operator","password":"operator123"}' | jq -r '.token')

if [ "$TOKEN" = "null" ] || [ -z "$TOKEN" ]; then
    echo -e "${RED}❌ Failed to get authentication token${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Authentication successful${NC}"
echo ""

# Function to make authenticated API calls
api_call() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "${BLUE}📡 $description${NC}"
    
    if [ -n "$data" ]; then
        response=$(curl -s -X $method "$url" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $TOKEN" \
            -d "$data" \
            -w "\n%{http_code}")
    else
        response=$(curl -s -X $method "$url" \
            -H "Authorization: Bearer $TOKEN" \
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

# 1. Show current system status
echo -e "${YELLOW}📊 Step 1: Current System Status${NC}"
echo "=================================="

api_call "GET" "http://localhost:8080/api/stations" "" "Getting all stations"
api_call "GET" "http://localhost:8080/api/bikes" "" "Getting all bikes"

# 2. Test bike reservation (as a user)
echo -e "${YELLOW}🚲 Step 2: Testing Bike Reservation${NC}"
echo "===================================="

# Get a bike ID from station 1
BIKE_ID=$(curl -s http://localhost:8080/api/bikes | jq -r '.[] | select(.stationId == 1 and .status == "AVAILABLE") | .id' | head -1)

if [ -n "$BIKE_ID" ] && [ "$BIKE_ID" != "null" ]; then
    echo -e "${PURPLE}🎯 Reserving bike $BIKE_ID from Downtown Central Station${NC}"
    api_call "POST" "http://localhost:8080/api/bikes/reserve" \
    "{
        \"stationId\": 1,
        \"userId\": 1,
        \"expiresAfterMinutes\": 15
    }" "Reserving bike for user 1"
else
    echo -e "${RED}❌ No available bikes at Downtown Central Station${NC}"
fi

# 3. Test bike checkout
echo -e "${YELLOW}🔓 Step 3: Testing Bike Checkout${NC}"
echo "================================="

if [ -n "$BIKE_ID" ] && [ "$BIKE_ID" != "null" ]; then
    api_call "POST" "http://localhost:8080/api/bikes/checkout" \
    "{
        \"bikeId\": \"$BIKE_ID\",
        \"userId\": 1
    }" "Checking out reserved bike"
else
    echo -e "${RED}❌ No bike available for checkout${NC}"
fi

# 4. Test bike return
echo -e "${YELLOW}🔒 Step 4: Testing Bike Return${NC}"
echo "==============================="

if [ -n "$BIKE_ID" ] && [ "$BIKE_ID" != "null" ]; then
    api_call "POST" "http://localhost:8080/api/bikes/return" \
    "{
        \"bikeId\": \"$BIKE_ID\",
        \"destinationStationId\": 2
    }" "Returning bike to University Campus Station"
else
    echo -e "${RED}❌ No bike available for return${NC}"
fi

# 5. Test bike movement (operator function)
echo -e "${YELLOW}🚚 Step 5: Testing Bike Movement (Operator)${NC}"
echo "============================================="

# Get a bike from station 2 to move to station 3
BIKE_TO_MOVE=$(curl -s http://localhost:8080/api/bikes | jq -r '.[] | select(.stationId == 2 and .status == "AVAILABLE") | .id' | head -1)

if [ -n "$BIKE_TO_MOVE" ] && [ "$BIKE_TO_MOVE" != "null" ]; then
    api_call "POST" "http://localhost:8080/api/bikes/move" \
    "{
        \"bikeId\": \"$BIKE_TO_MOVE\",
        \"sourceStationId\": 2,
        \"destinationStationId\": 3,
        \"operatorId\": 1
    }" "Moving bike from University Campus to Shopping Mall"
else
    echo -e "${RED}❌ No available bikes at University Campus to move${NC}"
fi

# 6. Test station status change (operator function)
echo -e "${YELLOW}🔧 Step 6: Testing Station Status Change${NC}"
echo "============================================="

api_call "PATCH" "http://localhost:8080/api/operator/stations/4/status" \
'{
    "status": "OUT_OF_SERVICE"
}' "Setting Park & Ride station to OUT_OF_SERVICE"

# 7. Final system status
echo -e "${YELLOW}📈 Step 7: Final System Status${NC}"
echo "==============================="

api_call "GET" "http://localhost:8080/api/stations" "" "Final station status"
api_call "GET" "http://localhost:8080/api/bikes" "" "Final bike status"

echo -e "${GREEN}🎉 Bike Management Operations Test Complete!${NC}"
echo ""
echo -e "${BLUE}📋 Summary of Operations Tested:${NC}"
echo "✅ Bike reservation (user operation)"
echo "✅ Bike checkout (user operation)"
echo "✅ Bike return (user operation)"
echo "✅ Bike movement between stations (operator operation)"
echo "✅ Station status change (operator operation)"
echo ""
echo -e "${YELLOW}🌐 Frontend Access:${NC}"
echo "• Open http://localhost:5173 in your browser"
echo "• Login as operator (username: operator, password: operator123)"
echo "• Navigate to Bike Management to see the interactive interface"

