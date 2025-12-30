# Student Management System

A comprehensive Spring Boot application for managing student information, courses, grades, and attendance with secure role-based access control.

## 🚀 Features

- **🔐 Authentication & Authorization**: JWT-based security with Admin, Teacher, and Student roles
- **👥 Student Management**: Full CRUD operations, profile management, and enrollment tracking
- **📚 Course Management**: Course creation, scheduling, and enrollment system
- **📊 Grade Management**: Grade entry, GPA calculation, and transcript generation
- **📅 Attendance Tracking**: Daily attendance marking with analytics and notifications
- **📈 Reporting**: Student performance reports, attendance statistics, and export to PDF/Excel

## 🛠️ Tech Stack

**Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA, Spring MVC  
**Database**: MySQL/PostgreSQL with Hibernate ORM  
**API**: RESTful endpoints with Swagger/OpenAPI documentation  
**Frontend**: Thymeleaf templates with Bootstrap 5 (optional)  
**Tools**: Maven, Docker, JUnit 5, Spring Boot Actuator  

## ⚡ Quick Start

```bash
# Clone and run
git clone <repo-url>
cd student-management-system
mvn spring-boot:run

# Access at: http://localhost:8080
# Default users: admin/admin123, teacher/teacher123, student/student123
```

## 📌 Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 14+
- Docker (optional)

---

📧 **API Docs**: `http://localhost:8080/swagger-ui.html`  
🐳 **Docker**: `docker-compose up -d`  
📄 **License**: MIT
