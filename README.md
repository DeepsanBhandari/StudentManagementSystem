# 🎓 Student Management System - Enterprise Grade REST API

A production-ready, secure REST API built with **Spring Boot 3** and **MongoDB**, featuring JWT-based authentication, role-based access control, and comprehensive student management capabilities.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0+-green.svg)](https://www.mongodb.com/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-blue.svg)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🚀 Key Features

### 🔐 Advanced Security Architecture
- **JWT-based Stateless Authentication** - Industry-standard token-based auth using HMAC-SHA512
- **Dual Token System** - Access tokens (15 min) + Refresh tokens (7 days) for enhanced security
- **BCrypt Password Encryption** - Industry-standard password hashing with salt
- **Role-Based Access Control (RBAC)** - Four distinct roles with granular permissions
- **Cryptographic Signature Verification** - Tamper-proof token validation
- **Automatic Token Expiration** - Built-in timestamp validation

### 👥 User Management System
- Complete user lifecycle management (CRUD operations)
- Multi-role support: **ADMIN**, **TEACHER**, **STUDENT**, **USER**
- Account activation/deactivation capabilities
- Secure registration with duplicate prevention
- Last login tracking and audit trail

### 📚 Student Profile Management
- **One-to-One User-Student Relationship** - Clean database design
- Comprehensive student profiles with academic data
- Department and year-based organization
- GPA tracking and academic progress monitoring
- Advanced search and filter capabilities
- Email uniqueness validation across system

### 🛡️ Enterprise-Level Security Features
- Custom JWT Authentication Filter integrated with Spring Security
- SecurityContext-based request authentication
- Protected endpoint authorization with method-level security
- Global exception handling with meaningful error responses
- Request validation using Jakarta Bean Validation

---

## 🏗️ Technical Architecture

### Technology Stack

**Backend Framework:**
- Spring Boot 3.2+
- Spring Security 6+
- Spring Data MongoDB
- Spring Web MVC

**Security & Authentication:**
- JJWT (JSON Web Token) 0.11.5
- BCrypt Password Encoder
- HMAC-SHA512 Cryptographic Signing

**Database:**
- MongoDB 7.0+ (NoSQL Document Database)
- Indexed fields for optimized queries
- Unique constraints for data integrity

**Additional Libraries:**
- Lombok - Boilerplate code reduction
- Jakarta Validation - Request validation
- SLF4J - Logging framework

### Project Structure

```
src/main/java/com/student/deep/
├── config/
│   └── SecurityConfig.java              # Spring Security configuration
├── controller/
│   ├── UserController.java              # User management endpoints
│   └── StudentController.java           # Student management endpoints
├── dto/
│   ├── LoginRequest.java                # Login credentials DTO
│   ├── LoginResponse.java               # JWT token response DTO
│   ├── RegisterRequest.java             # User registration DTO
│   ├── RefreshTokenRequest.java         # Token refresh DTO
│   └── UserResponse.java                # User data response DTO
├── exception/
│   ├── GlobalExceptionHandler.java      # Centralized error handling
│   ├── ResourceNotFoundException.java   # 404 exceptions
│   ├── DuplicateResourceException.java  # 409 conflict exceptions
│   └── ErrorResponse.java               # Error response structure
├── model/
│   ├── User.java                        # User entity with authentication data
│   └── Student.java                     # Student entity with academic data
├── repository/
│   ├── UserRepository.java              # User data access layer
│   └── StudentRepository.java           # Student data access layer
├── security/
│   ├── JwtAuthenticationFilter.java     # JWT validation filter
│   └── JwtTokenProvider.java            # Token generation & validation
└── service/
    ├── UserService.java                 # User business logic
    └── StudentService.java              # Student business logic
```

---

## 🔒 Security Implementation Deep Dive

### JWT Authentication Flow

#### 1️⃣ **User Login**
```
Client → POST /api/users/login
    ↓
JwtAuthenticationFilter (no token, allows public endpoint)
    ↓
SecurityConfig (permitAll for /login)
    ↓
UserService validates credentials with BCrypt
    ↓
JwtTokenProvider generates signed tokens
    ↓
Response: AccessToken + RefreshToken
```

#### 2️⃣ **Accessing Protected Endpoints**
```
Client → GET /api/users (with Authorization: Bearer <token>)
    ↓
JwtAuthenticationFilter extracts & validates token
    ├─ Verify HMAC-SHA512 signature
    ├─ Check expiration timestamp
    └─ Extract username & role from claims
    ↓
Set Authentication in SecurityContext
    ↓
SecurityConfig checks role permissions
    ↓
Controller executes if authorized
```

### Password Security
- **BCrypt Algorithm** with automatic salt generation
- **Work Factor**: 10 rounds (2^10 iterations)
- Passwords never stored in plain text
- Secure password comparison using constant-time algorithm

### JWT Token Structure
```json
{
  "header": {
    "alg": "HS512",
    "typ": "JWT"
  },
  "payload": {
    "sub": "username",
    "role": "ADMIN",
    "type": "ACCESS",
    "iat": 1641234567,
    "exp": 1641235467
  },
  "signature": "HMAC-SHA512(header.payload, secretKey)"
}
```

---

## 📊 Database Schema Design

### Users Collection
```javascript
{
  _id: ObjectId,
  username: String (unique, indexed),
  password: String (BCrypt hashed),
  email: String (unique, indexed),
  fullName: String,
  role: String (ADMIN|TEACHER|STUDENT|USER),
  active: Boolean,
  createdAt: ISODate,
  updatedAt: ISODate,
  lastLogin: ISODate
}
```

### Students Collection
```javascript
{
  _id: ObjectId,
  userId: String (unique, indexed, FK to User._id),
  firstName: String,
  lastName: String,
  email: String (unique, matches User.email),
  phoneNumber: String,
  address: String,
  department: String (indexed),
  year: Integer,
  gpa: Double,
  courses: Array,
  status: String (ACTIVE|INACTIVE|GRADUATED|SUSPENDED),
  createdAt: ISODate,
  updatedAt: ISODate
}
```

**Relationship**: One-to-One (User ↔ Student) via `userId` foreign key

---

## 🌐 API Endpoints

### 🔓 Public Endpoints (No Authentication Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user account |
| POST | `/api/users/login` | Login and receive JWT tokens |
| POST | `/api/users/refresh` | Refresh access token using refresh token |

### 🔐 Protected Endpoints

#### User Management (ADMIN Only)

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| GET | `/api/users` | Get all users | ADMIN |
| GET | `/api/users/{id}` | Get user by ID | ADMIN |
| GET | `/api/users/username/{username}` | Get user by username | ADMIN |
| GET | `/api/users/role/{role}` | Get users by role | ADMIN |
| PUT | `/api/users/{id}` | Update user details | ADMIN |
| DELETE | `/api/users/{id}` | Delete user | ADMIN |
| PATCH | `/api/users/{id}/activate` | Activate user account | ADMIN |
| PATCH | `/api/users/{id}/deactivate` | Deactivate user account | ADMIN |

#### Student Management (ADMIN + TEACHER)

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/students` | Create student profile | ADMIN, TEACHER |
| GET | `/api/students` | Get all students | ADMIN, TEACHER |
| GET | `/api/students/{id}` | Get student by ID | ADMIN, TEACHER |
| GET | `/api/students/user/{userId}` | Get student by user ID | ADMIN, TEACHER |
| GET | `/api/students/email/{email}` | Get student by email | ADMIN, TEACHER |
| GET | `/api/students/department/{dept}` | Get students by department | ADMIN, TEACHER |
| GET | `/api/students/year/{year}` | Get students by year | ADMIN, TEACHER |
| GET | `/api/students/status/{status}` | Get students by status | ADMIN, TEACHER |
| GET | `/api/students/gpa/{minGpa}` | Get students by minimum GPA | ADMIN, TEACHER |
| GET | `/api/students/search/firstName/{name}` | Search by first name | ADMIN, TEACHER |
| GET | `/api/students/search/lastName/{name}` | Search by last name | ADMIN, TEACHER |
| PUT | `/api/students/{id}` | Update student profile | ADMIN, TEACHER |
| DELETE | `/api/students/{id}` | Delete student profile | ADMIN |

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+** installed
- **Maven 3.8+** installed
- **MongoDB 7.0+** running locally or remotely
- **Postman** or **cURL** for API testing

### Installation Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/student-management-system.git
cd student-management-system
```

2. **Configure MongoDB**

Edit `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/student_management
spring.data.mongodb.database=student_management
```

3. **Configure JWT Secret**

⚠️ **IMPORTANT**: Change the JWT secret in production!

```properties
# Use a strong, randomly generated key (min 64 characters)
jwt.secret=your-very-secure-and-long-secret-key-minimum-512-bits-for-hs512-algorithm

# Token expiration times
jwt.access-token-expiration=900000       # 15 minutes
jwt.refresh-token-expiration=604800000   # 7 days
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

Application will start on `http://localhost:8080`

---

## 📝 Usage Examples

### 1. Register a New User

**Request:**
```bash
curl -X POST http://localhost:8080/api/users/register \
-H "Content-Type: application/json" \
-d '{
  "username": "john_admin",
  "password": "SecurePass123",
  "email": "john@university.edu",
  "fullName": "John Administrator",
  "role": "ADMIN"
}'
```

**Response:**
```json
{
  "id": "65b1a2c3d4e5f6a7b8c9d0e1",
  "username": "john_admin",
  "email": "john@university.edu",
  "fullName": "John Administrator",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-01-14T10:00:00",
  "lastLogin": null
}
```

### 2. Login and Receive JWT Tokens

**Request:**
```bash
curl -X POST http://localhost:8080/api/users/login \
-H "Content-Type: application/json" \
-d '{
  "username": "john_admin",
  "password": "SecurePass123"
}'
```

**Response:**
```json
{
  "userId": "65b1a2c3d4e5f6a7b8c9d0e1",
  "username": "john_admin",
  "email": "john@university.edu",
  "role": "ADMIN",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2FkbWluIiwicm9sZSI6IkFETUlOIiwidHlwZSI6IkFDQ0VTUyIsImlhdCI6MTY0MTIzNDU2NywiZXhwIjoxNjQxMjM1NDY3fQ.signature",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2FkbWluIiwidHlwZSI6IlJFRlJFU0giLCJpYXQiOjE2NDEyMzQ1NjcsImV4cCI6MTY0MTgzOTM2N30.signature",
  "tokenType": "Bearer",
  "expiresIn": 900000,
  "message": "Login successful",
  "success": true
}
```

### 3. Access Protected Endpoint

**Request:**
```bash
curl -X GET http://localhost:8080/api/users \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### 4. Create Student Profile

**Request:**
```bash
curl -X POST http://localhost:8080/api/students \
-H "Authorization: Bearer <TEACHER_OR_ADMIN_TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "userId": "65b1a2c3d4e5f6a7b8c9d0e3",
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice@university.edu",
  "phoneNumber": "+1234567890",
  "address": "123 Campus Drive",
  "department": "Computer Science",
  "year": 2,
  "gpa": 3.8
}'
```

### 5. Refresh Access Token

**Request:**
```bash
curl -X POST http://localhost:8080/api/users/refresh \
-H "Content-Type: application/json" \
-d '{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
}'
```

---

## 🔐 Role-Based Access Control (RBAC)

### Permission Matrix

| Feature | STUDENT | TEACHER | ADMIN | Public |
|---------|---------|---------|-------|--------|
| Register Account | ❌ | ❌ | ❌ | ✅ |
| Login | ❌ | ❌ | ❌ | ✅ |
| Refresh Token | ❌ | ❌ | ❌ | ✅ |
| View All Users | ❌ | ❌ | ✅ | ❌ |
| Manage Users | ❌ | ❌ | ✅ | ❌ |
| View Students | ❌ | ✅ | ✅ | ❌ |
| Create Student | ❌ | ✅ | ✅ | ❌ |
| Update Student | ❌ | ✅ | ✅ | ❌ |
| Delete Student | ❌ | ❌ | ✅ | ❌ |

---

## 🛡️ Security Best Practices Implemented

### ✅ Authentication & Authorization
- Stateless JWT authentication (no server-side sessions)
- Cryptographically signed tokens with HMAC-SHA512
- Role-based access control at endpoint level
- Automatic token expiration and validation

### ✅ Password Security
- BCrypt hashing with automatic salt generation
- Passwords never exposed in API responses
- Secure password comparison using constant-time algorithm

### ✅ Data Validation
- Jakarta Bean Validation on all input DTOs
- Email format validation
- Unique constraint enforcement (username, email)
- Role validation against predefined enum values

### ✅ Error Handling
- Global exception handler for consistent error responses
- No sensitive information in error messages
- Appropriate HTTP status codes (401, 403, 404, 409, 500)

### ✅ Database Security
- Indexed fields for query optimization
- Unique constraints on critical fields
- One-to-one relationship enforcement

---

## 📈 Performance Optimizations

- **Indexed MongoDB Fields**: username, email, userId, department
- **Stateless Architecture**: No session storage overhead
- **Optimized Queries**: Repository methods use indexed fields
- **DTO Pattern**: Prevents over-fetching and exposure of sensitive data
- **Lazy Loading**: Only necessary data is loaded per request

---

## 🐛 Exception Handling

### Custom Exceptions

| Exception | HTTP Status | Use Case |
|-----------|-------------|----------|
| `ResourceNotFoundException` | 404 Not Found | User/Student not found |
| `DuplicateResourceException` | 409 Conflict | Username/email already exists |
| `IllegalArgumentException` | 400 Bad Request | Invalid role, validation errors |
| `MethodArgumentNotValidException` | 400 Bad Request | Request body validation failure |

### Error Response Format
```json
{
  "timestamp": "2026-01-14T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with username: johndoe",
  "path": "/api/users/username/johndoe"
}
```

---

## 🧪 Testing the API

### Using Postman

1. **Import Environment Variables**
```json
{
  "BASE_URL": "http://localhost:8080",
  "ADMIN_TOKEN": "",
  "TEACHER_TOKEN": "",
  "STUDENT_TOKEN": ""
}
```

2. **Auto-Save Token Script** (Add to Login request Tests tab)
```javascript
const response = pm.response.json();
if (response.success && response.accessToken) {
    pm.environment.set("ADMIN_TOKEN", response.accessToken);
}
```

3. **Use Token in Requests**
```
Authorization: Bearer {{ADMIN_TOKEN}}
```

### Using cURL

Save token to variable:
```bash
TOKEN=$(curl -X POST http://localhost:8080/api/users/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"pass"}' \
| jq -r '.accessToken')

# Use in subsequent requests
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/users
```

---

## 📊 Application Configuration

### application.properties
```properties
# Server Configuration
server.port=8080
spring.application.name=Student Management System

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/student_management
spring.data.mongodb.database=student_management

# JWT Configuration
jwt.secret=your-secret-key-here-minimum-64-characters-for-hs512
jwt.access-token-expiration=900000
jwt.refresh-token-expiration=604800000

# Logging
logging.level.com.student.deep=DEBUG
logging.level.org.springframework.security=DEBUG

# Error Handling
server.error.include-message=always
server.error.include-binding-errors=always
```

---

## 🔄 Request/Response Flow Diagram

```
┌──────────┐
│  Client  │
└────┬─────┘
     │ 1. POST /login (username, password)
     ▼
┌─────────────────────────┐
│ JwtAuthenticationFilter │ → No token, allows public endpoint
└────────┬────────────────┘
         │ 2. Continues
         ▼
┌─────────────────────┐
│   SecurityConfig    │ → .permitAll() for /login
└────────┬────────────┘
         │ 3. Authorized
         ▼
┌─────────────────┐
│ UserController  │ → Validates @Valid annotations
└────────┬────────┘
         │ 4. Calls service
         ▼
┌──────────────┐
│ UserService  │ → Find user, verify password (BCrypt)
└────────┬─────┘
         │ 5. Calls token provider
         ▼
┌───────────────────┐
│ JwtTokenProvider  │ → Generate signed JWT tokens
└────────┬──────────┘
         │ 6. Returns tokens
         ▼
┌─────────────────┐
│ LoginResponse   │ → accessToken + refreshToken
└────────┬────────┘
         │ 7. HTTP 200 OK
         ▼
┌──────────┐
│  Client  │ → Stores tokens
└──────────┘
```

---

## 🎯 Key Learning Outcomes

This project demonstrates expertise in:

### Backend Development
- ✅ RESTful API design principles
- ✅ Spring Boot 3 and Spring Security 6
- ✅ MongoDB integration and schema design
- ✅ Dependency injection and IoC
- ✅ DTO pattern for data transfer

### Security Engineering
- ✅ JWT authentication implementation
- ✅ Cryptographic signing (HMAC-SHA512)
- ✅ Password hashing (BCrypt)
- ✅ Role-based access control
- ✅ Stateless authentication architecture

### Software Architecture
- ✅ Layered architecture (Controller → Service → Repository)
- ✅ Separation of concerns
- ✅ Global exception handling
- ✅ Custom filter integration
- ✅ Database relationship design

### Best Practices
- ✅ Input validation
- ✅ Error handling
- ✅ Logging and debugging
- ✅ Code organization
- ✅ Security-first approach

---

## 🚀 Future Enhancements

- [ ] Implement email verification for registration
- [ ] Add password reset functionality
- [ ] Implement course enrollment system
- [ ] Add grade management features
- [ ] Create attendance tracking module
- [ ] Add file upload for student documents
- [ ] Implement pagination for large datasets
- [ ] Add API rate limiting
- [ ] Create comprehensive unit and integration tests
- [ ] Add Swagger/OpenAPI documentation
- [ ] Implement audit logging
- [ ] Add real-time notifications

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/DeepsanBhandari/)
- Email: deepsanbhandari7@gmail.com
---

## 🙏 Acknowledgments

- Spring Framework team for excellent documentation
- MongoDB community for NoSQL best practices
- JWT.io for token debugging tools
- Stack Overflow community for problem-solving

---

## 📞 Contact & Support

For questions, issues, or contributions:
- Open an issue on [GitHub Issues](https://github.com/DeepsanBhandari/StudentManagementSystem/edit/)
- Email: deepsanbhandari7@gmail.com

---

<div align="center">

**⭐ If you found this project helpful, please give it a star! ⭐**

Made with ❤️ using Spring Boot, MongoDB, and JWT

</div>
