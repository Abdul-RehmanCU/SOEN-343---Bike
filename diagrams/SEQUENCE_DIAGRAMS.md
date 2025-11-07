# QwikRide Sequence Diagrams

## Overview
These diagrams show the flow of operations for key user interactions in the system.

---

## 1. User Registration Flow

Shows how a new user registers for an account.

```plantuml
@startuml
actor Guest
participant "Register Page" as UI
participant "API Service" as API
participant "Registration Controller" as Controller
participant "Registration Service" as Service
participant "User Repository" as Repo
participant "Password Encoder" as Encoder
database "H2 Database" as DB

Guest -> UI: Fill registration form
Guest -> UI: Submit registration

UI -> API: POST /api/auth/register\n(fullName, email, username, password, etc.)
API -> Controller: registerUser(RegistrationRequestDTO)

Controller -> Service: registerUser(dto)

Service -> Repo: existsByUsername(username)
Repo -> DB: Query username
DB --> Repo: false
Repo --> Service: false

Service -> Repo: existsByEmail(email)
Repo -> DB: Query email
DB --> Repo: false
Repo --> Service: false

Service -> Encoder: encode(password)
Encoder --> Service: hashedPassword

Service -> Service: Create User entity\n(set role to RIDER)

Service -> Repo: save(user)
Repo -> DB: Insert user record
DB --> Repo: Saved user
Repo --> Service: User object

Service --> Controller: Success message
Controller --> API: 201 CREATED
API --> UI: Registration successful
UI --> Guest: Show success message\nRedirect to login

@enduml
```

### Flow Description

1. **Guest fills form**: User enters their details on the registration page
2. **Validation**: System checks if username and email are unique
3. **Password hashing**: Password is encrypted using BCrypt before storage
4. **User creation**: New user is saved with RIDER role by default
5. **Success**: User is redirected to login page

### Error Scenarios

- If username exists → Return 400 with "Username already taken"
- If email exists → Return 400 with "Email already registered"
- If validation fails → Return 400 with validation errors

---

## 2. User Login Flow

Shows how a user authenticates and gets a JWT token.

```plantuml
@startuml
actor User
participant "Login Page" as UI
participant "API Service" as API
participant "Auth Controller" as Controller
participant "Auth Service" as Service
participant "User Repository" as Repo
participant "Password Encoder" as Encoder
participant "JWT Util" as JWT
database "H2 Database" as DB

User -> UI: Enter username and password
User -> UI: Click login

UI -> API: POST /api/auth/login\n(username, password)
API -> Controller: login(LoginRequestDTO)

Controller -> Service: authenticate(username, password)

Service -> Repo: findByUsername(username)
Repo -> DB: Query user
DB --> Repo: User record
Repo --> Service: User object

Service -> Encoder: matches(password, user.passwordHash)
Encoder --> Service: true

Service -> JWT: generateToken(username, role)
JWT -> JWT: Create JWT with claims\n(username, role, expiration)
JWT --> Service: JWT token

Service -> Service: Create LoginResponseDTO\n(token, username, fullName, role)

Service --> Controller: LoginResponseDTO
Controller --> API: 200 OK + response
API --> UI: Token and user info

UI -> UI: Store token in localStorage
UI -> UI: Update auth context
UI --> User: Redirect to dashboard

@enduml
```

### Flow Description

1. **User enters credentials**: Username and password submitted
2. **User lookup**: System finds user by username
3. **Password verification**: BCrypt compares hashed passwords
4. **Token generation**: JWT token created with user info and role
5. **Response**: Token sent to frontend and stored in localStorage
6. **Redirect**: User sent to role-based dashboard

### Error Scenarios

- If username not found → Return 401 "Invalid credentials"
- If password incorrect → Return 401 "Invalid credentials"
- If validation fails → Return 400 with validation errors

---

## 3. Authenticated Request Flow

Shows how protected endpoints verify user identity.

```plantuml
@startuml
actor User
participant "Frontend" as UI
participant "API Service" as API
participant "Spring Security" as Security
participant "JWT Filter" as Filter
participant "JWT Util" as JWT
participant "Controller" as Controller

User -> UI: Access protected feature
UI -> UI: Get token from localStorage

UI -> API: API request\nAuthorization: Bearer <token>

API -> Security: Intercept request
Security -> Filter: doFilterInternal()

Filter -> Filter: Extract token from header
Filter -> JWT: validateToken(token)
JWT -> JWT: Verify signature\nCheck expiration
JWT --> Filter: Valid

Filter -> JWT: extractUsername(token)
JWT --> Filter: username

Filter -> JWT: extractRole(token)
JWT --> Filter: role

Filter -> Security: Set authentication\nin security context
Security -> Controller: Forward request

Controller -> Controller: Process business logic
Controller --> API: Response
API --> UI: Data
UI --> User: Display result

@enduml
```

### Flow Description

1. **Token included**: Frontend adds JWT to Authorization header
2. **Token validation**: Spring Security filter validates token
3. **Extract claims**: Username and role extracted from token
4. **Set context**: User authentication set in security context
5. **Process request**: Controller handles the business logic
6. **Return response**: Data sent back to frontend

### Error Scenarios

- If token missing → Return 401 "Unauthorized"
- If token expired → Return 401 "Token expired"
- If token invalid → Return 401 "Invalid token"
- If user lacks permission → Return 403 "Forbidden"

---

## 4. Rider Reserves and Checks Out Bike Flow

Shows how a rider reserves a bike through the Dashboard UI and proceeds to checkout.

```plantuml
@startuml
actor Rider
participant "Dashboard" as Dashboard
participant "MapDashboard" as MapUI
participant "StationDetailsPanel" as Panel
participant "API Service" as API
participant "Bike Controller" as Controller
participant "Bike Service" as Service
participant "Bike Repository" as BikeRepo
participant "Station Repository" as StationRepo
participant "SystemConsole" as Console
database "H2 Database" as DB

Rider -> Dashboard: Navigate to /dashboard
Dashboard -> Dashboard: Render DashboardHome

Rider -> Dashboard: Click "Map View" card
Dashboard -> MapUI: Switch to MapDashboard view

MapUI -> API: GET /api/stations
API -> Controller: getAllStations()
Controller -> Service: getAllStations()
Service -> StationRepo: findAll()
StationRepo -> DB: Query stations
DB --> StationRepo: Station records
StationRepo --> Service: List<Station>
Service --> Controller: List<Station>
Controller --> API: 200 OK + stations
API --> MapUI: Station data

MapUI -> API: GET /api/bikes
API -> Controller: getAllBikes()
Controller -> Service: getAllBikes()
Service -> BikeRepo: findAll()
BikeRepo -> DB: Query bikes
DB --> BikeRepo: Bike records
BikeRepo --> Service: List<Bike>
Service --> Controller: List<Bike>
Controller --> API: 200 OK + bikes
API --> MapUI: Bike data

MapUI -> MapUI: Render map with station markers\n(color-coded by availability)
MapUI --> Rider: Display interactive map

Rider -> MapUI: Click station marker
MapUI -> Panel: Open StationDetailsPanel(station, bikes)

Panel -> Panel: Calculate available bikes\nRender dock grid
Panel --> Rider: Show station details\n(bikes: 15/25, 10 free docks)

Rider -> Panel: Click "Reserve Bike" button

Panel -> API: POST /api/bikes/reserve\n{stationId, userId, expiresAfterMinutes: 15}
API -> Controller: reserveBike(ReservationRequestDTO)

Controller -> Service: reserveBike(stationId, userId, minutes)

Service -> StationRepo: findById(stationId)
StationRepo -> DB: Query station
DB --> StationRepo: Station
StationRepo --> Service: Station

alt Station is OUT_OF_SERVICE
    Service --> Controller: throw Exception
    Controller --> API: 400 Bad Request
    API --> Panel: Error response
    Panel -> Console: addMessage("Cannot reserve from\nout-of-service station", ERROR)
    Console --> Rider: Display error message
else Station has available bikes
    Service -> BikeRepo: findAvailableBikeAtStation(stationId)
    BikeRepo -> DB: Query available bike
    DB --> BikeRepo: Bike (AVAILABLE)
    BikeRepo --> Service: Bike
    
    Service -> Service: bike.reserve(userId, 15)\nSet status = RESERVED\nSet reservationExpiresAt
    
    Service -> BikeRepo: save(bike)
    BikeRepo -> DB: Update bike record
    DB --> BikeRepo: Updated bike
    BikeRepo --> Service: Bike
    
    Service --> Controller: Reserved Bike
    Controller --> API: 200 OK + bike
    API --> Panel: Success
    
    Panel -> MapUI: loadData() (refresh)
    MapUI -> Console: addMessage("Bike reserved successfully\n(expires in 15 minutes)", SUCCESS)
    Console --> Rider: Show success message
    Panel -> Panel: Close panel
end

note right of Rider
  Rider has 15 minutes
  to checkout the bike
  before reservation expires
end note

Rider -> MapUI: Locate reserved bike
MapUI -> MapUI: Filter bikes by status=RESERVED\nand userId

Rider -> Panel: Open station with reserved bike
Panel --> Rider: Show bike details\n(Status: RESERVED)

Rider -> Panel: Click bike or use "Checkout" action

Panel -> API: POST /api/bikes/checkout\n{bikeId, userId}
API -> Controller: checkoutBike(CheckoutRequestDTO)

Controller -> Service: checkoutBike(bikeId, userId)

Service -> BikeRepo: findById(bikeId)
BikeRepo -> DB: Query bike
DB --> BikeRepo: Bike (RESERVED)
BikeRepo --> Service: Bike

alt Bike not reserved by this user
    Service --> Controller: throw Exception
    Controller --> API: 403 Forbidden
    API --> Panel: Error response
    Panel -> Console: addMessage("Cannot checkout\nthis bike", ERROR)
else Valid checkout
    Service -> Service: bike.checkout(userId)\nSet status = IN_USE\nRecord startTime
    
    Service -> BikeRepo: save(bike)
    BikeRepo -> DB: Update bike record
    DB --> BikeRepo: Updated bike
    BikeRepo --> Service: Bike
    
    Service --> Controller: Bike in use
    Controller --> API: 200 OK + bike
    API --> Panel: Success
    
    Panel -> MapUI: loadData() (refresh)
    MapUI -> Console: addMessage("Bike checked out successfully\nEnjoy your ride!", SUCCESS)
    Console --> Rider: Show success message
    
    Panel --> Rider: Display trip started\n(Bike ID, Start time)
end

@enduml
```

### Flow Description

1. **Navigate to Dashboard**: Rider accesses the dashboard and sees the home view with cards
2. **Switch to Map View**: Rider clicks "Map View" card to see interactive map
3. **Load Map Data**: System fetches all stations and bikes, renders map with color-coded markers
4. **Select Station**: Rider clicks a station marker to view details
5. **View Station Details**: Panel shows dock grid, available bikes, and capacity
6. **Reserve Bike**: Rider clicks "Reserve Bike" button
   - System validates station is in service
   - Finds an available bike at the station
   - Changes bike status to RESERVED
   - Sets 15-minute expiration timer
7. **Confirmation**: Success message appears in system console
8. **Checkout Bike**: Within 15 minutes, rider returns to reserved bike
   - System validates rider owns the reservation
   - Changes bike status to IN_USE
   - Records trip start time
9. **Trip Started**: Rider can now ride the bike

### Business Rules

- **DM-05**: Reservation disabled if bikesAvailable == 0 or station OUT_OF_SERVICE
- **Reservation expires**: After 15 minutes, bike becomes AVAILABLE again
- **Single reservation**: Rider can only have one active reservation at a time
- **Checkout validation**: Only the rider who reserved can checkout that specific bike

### Error Scenarios

- If station is OUT_OF_SERVICE → Return 400 "Cannot reserve from out-of-service station"
- If no bikes available → Return 400 "No bikes available at this station"
- If rider already has reservation → Return 400 "Already have an active reservation"
- If checkout wrong bike → Return 403 "Cannot checkout this bike"
- If reservation expired → Return 400 "Reservation has expired"

### Console Messages

**Success Messages (Green):**
- "System loaded successfully"
- "Bike reserved successfully (expires in 15 minutes)"
- "Bike checked out successfully. Enjoy your ride!"

**Error Messages (Red):**
- "Cannot reserve bike from out-of-service station"
- "No bikes available at this station"
- "Cannot checkout this bike"
- "Reservation has expired"

---

## Key Components

### Frontend
- **Pages**: React components for UI
- **API Service**: Axios instance for HTTP requests
- **Auth Context**: Global authentication state

### Backend
- **Controllers**: Handle HTTP requests and responses
- **Services**: Business logic layer
- **Repositories**: Database access layer
- **Security**: JWT filter and configuration
- **DTOs**: Data transfer objects for API communication

### Security
- **BCrypt**: Password hashing (one-way encryption)
- **JWT**: Stateless authentication tokens
- **Spring Security**: Framework for authentication and authorization

