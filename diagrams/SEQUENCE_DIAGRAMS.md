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

