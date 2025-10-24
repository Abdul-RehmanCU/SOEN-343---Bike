#!/bin/bash

echo "=== QwikRide Bike Management System Test ==="

# Base URL for the backend API
BASE_URL="http://localhost:8080/api"

# Get authentication token
echo -e "\n1. Getting authentication token..."
AUTH_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"operator","password":"operator123"}')
TOKEN=$(echo "$AUTH_RESPONSE" | grep -oP '"token":"\K[^"]+')
echo "Token: $TOKEN"

# Create a test station first
echo -e "\n2. Creating a test station..."
STATION_RESPONSE=$(curl -s -X POST "$BASE_URL/stations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Test Station","address":"123 Test St","capacity":10}')
STATION_ID=$(echo "$STATION_RESPONSE" | grep -oP '"id":\K[0-9]+')
echo "Station ID: $STATION_ID"

# Create bikes
echo -e "\n3. Creating bikes..."
STANDARD_BIKE_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"type\":\"STANDARD\",\"stationId\":$STATION_ID}")
STANDARD_BIKE_ID=$(echo "$STANDARD_BIKE_RESPONSE" | grep -oP '"id":"\K[^"]+')
echo "Standard Bike ID: $STANDARD_BIKE_ID"

EBIKE_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"type\":\"E_BIKE\",\"stationId\":$STATION_ID}")
EBIKE_ID=$(echo "$EBIKE_RESPONSE" | grep -oP '"id":"\K[^"]+')
echo "E-Bike ID: $EBIKE_ID"

# Get all bikes
echo -e "\n4. Getting all bikes..."
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/bikes" | jq .

# Reserve a bike
echo -e "\n5. Reserving a bike..."
RESERVE_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/reserve" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"stationId\":$STATION_ID,\"userId\":1,\"expiresAfterMinutes\":15}")
echo "Reservation Response: $RESERVE_RESPONSE"

# Checkout a bike
echo -e "\n6. Checking out a bike..."
CHECKOUT_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/checkout" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"bikeId\":\"$STANDARD_BIKE_ID\",\"userId\":1}")
echo "Checkout Response: $CHECKOUT_RESPONSE"

# Return a bike
echo -e "\n7. Returning a bike..."
RETURN_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/return" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"bikeId\":\"$STANDARD_BIKE_ID\",\"returnStationId\":$STATION_ID,\"userId\":1,\"durationMinutes\":30.5,\"distanceKm\":5.2}")
echo "Return Response: $RETURN_RESPONSE"

# Move a bike (rebalancing)
echo -e "\n8. Moving a bike (rebalancing)..."
MOVE_RESPONSE=$(curl -s -X POST "$BASE_URL/bikes/move" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"bikeId\":\"$EBIKE_ID\",\"newStationId\":$STATION_ID,\"operatorId\":1}")
echo "Move Response: $MOVE_RESPONSE"

# Get bikes by station
echo -e "\n9. Getting bikes by station..."
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/bikes/station/$STATION_ID" | jq .

# Process expired reservations
echo -e "\n10. Processing expired reservations..."
curl -s -X POST -H "Authorization: Bearer $TOKEN" "$BASE_URL/bikes/expired-reservations/process"

echo -e "\n=== Test Complete ==="
