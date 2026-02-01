# Spring Boot Part 2: Spring Data JPA and Database Integration

## 📋 Experiment Information
- **University**: Birzeit University
- **Faculty**: Engineering and Technology
- **Department**: Electrical and Computer Engineering
- **Course**: Advanced Computer Systems Engineering Lab (ENCS515)
- **Experiment No.**: 10
- **Semester**: Spring
- **Student**: Layan Buirat (1211439)
- **GitHub Repository**: `spring-boot-jpa-lab-1211439`

## 🎯 Experiment Objectives
1. Understand the fundamentals of Spring Data JPA framework
2. Create mapping between Java models and relational database models
3. Connect and perform CRUD operations to MySQL database using Spring Data JPA

## 🏗️ Prerequisites & Requirements
- **Knowledge**: Java programming language
- **Architecture**: Basic understanding of MVC (Model-View-Controller)
- **Tools**: Maven, MySQL Server/Workbench, Postman
- **Databases**: Basic knowledge of relational databases (MySQL)
- **Frameworks**: Basic idea about Spring framework (preferably Spring MVC)
- **ORM**: Knowledge of Java ORM models is helpful but not required

## 📚 Key Concepts

### 🔹 ORM (Object-Relational Mapping)
Technique to convert data between object-oriented models and relational database models, providing database abstraction and decoupling.

### 🔹 JPA (Java Persistence API)
Specification that provides Java developers with ORM to manage relational databases by mapping entity classes to SQL tables.

### 🔹 Spring Data JPA
Framework built on top of Hibernate that implements repositories to retrieve and manipulate data in databases.

## 🚀 Project Setup

### Step 1: Create Spring Boot Project
Using Spring Initializr with the following dependencies:
- **Spring Web** - For building web applications and RESTful APIs
- **MySQL Driver** - JDBC driver for MySQL database connectivity
- **Spring Data JPA** - For ORM and database operations

### Step 2: Application Configuration
Configure `src/main/resources/application.properties`:

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/MyDB?useSSL=false
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

### Step 3: Project Structure
```
com.example.demo
├── controllers/
├── models/
├── repositories/
└── services/
```

## 🗄️ Entity Mapping

### User Entity Class
```java
package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String userName;
    
    // Constructors, getters, and setters
}
```

## 📦 Repository Layer

### UserRepository Interface
```java
package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
```

## 🛠️ Service Layer

### UserService Class
```java
package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    public List<User> getAll() {
        return userRepository.findAll();
    }
    
    public String addUser(User user) {
        userRepository.save(user);
        return "success";
    }
    
    public String deleteUser(Integer userId) {
        userRepository.deleteById(userId);
        return "success";
    }
}
```

## 🌐 Controller Layer (REST APIs)

### UserController Class
```java
package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import java.util.List;

@RestController
public class UserController {
    
    @Autowired
    UserService service;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAll();
    }
    
    @PostMapping("/users")
    public String addOne(@RequestBody User user) {
        return service.addUser(user);
    }
    
    @DeleteMapping("/users/{id}")
    public String deleteOne(@PathVariable Integer id) {
        return service.deleteUser(id);
    }
}
```

## 🔗 Entity Relationships

### 1️⃣ One-to-One Relationship (User ↔ Office)
```java
// In Office entity
@OneToOne(cascade = CascadeType.PERSIST)
@JoinColumn(name = "user_id", referencedColumnName = "user_id")
private User user;

// In User entity (for bidirectional)
@OneToOne(mappedBy = "user")
private Office office;
```

### 2️⃣ One-to-Many Relationship (User ↔ Account)
```java
// In User entity
@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
private List<Account> accounts = new ArrayList<>();

// In Account entity
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

### 3️⃣ Many-to-Many Relationship (User ↔ Course)
```java
// In User entity
@ManyToMany(cascade = CascadeType.PERSIST)
@JoinTable(
    name = "user_course",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private List<Course> courses = new ArrayList<>();

// In Course entity (for bidirectional)
@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "courses")
private List<User> users;
```

## 🧪 Testing with Postman

### API Endpoints:
- **GET** `http://localhost:8081/users` - Retrieve all users
- **POST** `http://localhost:8081/users` - Create a new user
- **DELETE** `http://localhost:8081/users/{id}` - Delete a user by ID

### Sample JSON Request Body:
```json
{
    "userName": "LayanB"
}
```

## 📝 Key Annotations Explained

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as JPA entity |
| `@Table` | Specifies table name |
| `@Id` | Marks field as primary key |
| `@GeneratedValue` | Configures primary key generation |
| `@Column` | Configures column properties |
| `@OneToOne` | Defines one-to-one relationship |
| `@OneToMany` | Defines one-to-many relationship |
| `@ManyToOne` | Defines many-to-one relationship |
| `@ManyToMany` | Defines many-to-many relationship |
| `@JoinColumn` | Specifies foreign key column |
| `@JoinTable` | Configures join table for relationships |
| `@Autowired` | Enables dependency injection |

## 💡 Best Practices
1. Always use wrapper classes (Integer, Long) for ID fields in JPA entities
2. Implement proper encapsulation with private fields and public getters/setters
3. Use meaningful table and column names
4. Handle cascading operations appropriately
5. Implement proper exception handling in services
6. Use DTOs (Data Transfer Objects) for API responses when needed

## 🚦 Running the Application
1. Start MySQL server and create `MyDB` database
2. Update database credentials in `application.properties`
3. Run the Spring Boot application
4. Test APIs using Postman or browser
5. Verify data in MySQL Workbench

## 🔄 Database Operations
Spring Data JPA provides built-in methods:
- `save()` - Insert or update
- `findAll()` - Retrieve all records
- `findById()` - Find by primary key
- `deleteById()` - Delete by ID
- Custom queries using method names or `@Query` annotation


This experiment demonstrates practical implementation of Spring Boot with Spring Data JPA for database operations, following industry best practices and MVC architecture principles.
