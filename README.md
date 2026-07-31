# 🌱 GreenThumb - E-Commerce Platform Backend

A **production-ready e-commerce platform** demonstrating advanced Spring Boot architecture, secure authentication, and scalable microservices design patterns.

> **Built with**: Java 23 • Spring Boot 4.1.0 • PostgreSQL • JWT • REST APIs • Docker

---

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Design Patterns & Best Practices](#design-patterns--best-practices)
- [Database Schema](#database-schema)
- [Security](#security)
- [Performance Highlights](#performance-highlights)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

---

## 🎯 Overview

**GreenThumb** is a full-stack e-commerce platform specialized in plant sales. It demonstrates enterprise-grade backend engineering practices including:

- ✅ Secure JWT-based authentication with role-based authorization
- ✅ Layered architecture with clear separation of concerns
- ✅ RESTful API design with 13+ endpoints
- ✅ PostgreSQL database with optimized queries
- ✅ Comprehensive input validation and exception handling
- ✅ Production-ready code patterns and practices

**Perfect for**: Portfolio showcase, system design study, Spring Boot reference implementation

---

## ✨ Key Features

### 🔐 **Security & Authentication**
- JWT-based stateless authentication (no server sessions)
- BCrypt password hashing (industry-standard)
- Role-Based Access Control (RBAC) - User vs Admin roles
- Spring Security integration with custom filters
- Secure API endpoints with authorization checks

### 🛍️ **E-Commerce Features**
- 📦 **Product Catalog** - Browse plants by type and supplier
- 🛒 **Shopping Cart** - Add/update/remove items with status tracking
- ⭐ **Wishlist** - Save favorite plants for later
- 📋 **Order Management** - Checkout and order history
- 👥 **User Profiles** - Account management with role assignment

### 🏗️ **Architecture & Design**
- Layered architecture (Controller → Service → Repository → Domain)
- DTO pattern for API contracts
- Repository pattern for data abstraction
- Dependency injection (constructor-based)
- Global exception handling
- Automatic entity auditing (CreatedDate, LastModifiedDate)

### 📊 **Data Management**
- PostgreSQL with proper relationships
- Query optimization mindset
- Cascading deletes configured
- Foreign key constraints
- Indexed frequently-queried columns

---

## 💻 Tech Stack

### **Backend**
| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 23 |
| Framework | Spring Boot | 4.1.0 |
| Security | Spring Security + JWT | JJWT 0.12.6 |
| ORM | JPA/Hibernate | Latest |
| Database | PostgreSQL | 12+ |
| Build Tool | Maven | 3.6+ |
| Containerization | Docker | Latest |

### **Key Dependencies**
```xml
<!-- Core Spring -->
<spring-boot-starter-web>
<spring-boot-starter-data-jpa>
<spring-boot-starter-security>

<!-- JWT -->
<jjwt-api>
<jjwt-impl>
<jjwt-jackson>

<!-- Database -->
<postgresql-driver>
<spring-boot-starter-validation>

<!-- Lombok (Code Generation) -->
<lombok>
```

---

## 🏛️ Architecture

### **Layered Design Pattern**

```
┌─────────────────────────────────────────────┐
│         REST API Layer (Controllers)        │
│   - HTTP request/response handling          │
│   - Input validation with @Valid            │
│   - Status code management                  │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│       Service Layer (Business Logic)        │
│   - Transaction management                  │
│   - Business rule enforcement               │
│   - Data transformation                     │
│   - Security checks                         │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│   Repository Layer (Data Access)            │
│   - JPA queries                             │
│   - Database abstraction                    │
│   - Easy to mock for testing                │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│    Domain Layer (JPA Entities)              │
│   - @Entity classes                         │
│   - Relationships (ManyToOne, OneToMany)    │
│   - Auditable base class                    │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         PostgreSQL Database                 │
│   - Normalized schema                       │
│   - Foreign key constraints                 │
│   - Optimized indexes                       │
└─────────────────────────────────────────────┘
```

### **Request Flow Example: Add to Cart**

```
1. User sends: POST /api/cart/items { plantId: 1, quantity: 2 }
                           ↓
2. Controller validates input (@Valid on CartItemRequestDTO)
                           ↓
3. Service retrieves plant, calculates total
                           ↓
4. Repository saves CartItem to database
                           ↓
5. Controller returns: CartResponseDTO with updated cart
                           ↓
6. User receives 201 Created with cart details
```

---

## 🚀 Getting Started

### **Prerequisites**

```bash
# Required
Java 23 JDK
PostgreSQL 12+
Maven 3.6+

# Optional
Docker (for containerization)
Git (for version control)
```

### **Database Setup**

```bash
# Create database
createdb greenthumb

# Create user (if not exists)
createuser greenthumb

# Set password
psql -d greenthumb -c "ALTER USER greenthumb WITH PASSWORD 'greenthumb';"

# Grant privileges
psql -d greenthumb -c "GRANT ALL PRIVILEGES ON DATABASE greenthumb TO greenthumb;"
```

### **Run the Application**

```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/GreenThumb.git
cd GreenThumb-main

# Build project
./mvnw clean package

# Run application
./mvnw spring-boot:run
```

**Server starts at**: `http://localhost:8080`

### **Test Credentials (Pre-seeded)**

```
Email: admin@example.com
Password: admin123
Role: ADMIN
```

### **Try an API Endpoint**

```bash
# Get all plants (public endpoint)
curl http://localhost:8080/api/plants

# Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'

# Use token in subsequent requests
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/cart
```

---

## 📡 API Endpoints

### **Authentication** (`/api/auth`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| POST | `/register` | ❌ | Create new user account |
| POST | `/login` | ❌ | Login and get JWT token |

**Example Request:**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk0..."
}
```

### **Plants** (`/api/plants`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ❌ | List all plants |
| GET | `/{id}` | ❌ | Get plant details |
| POST | `/` | ✅ | Create plant |
| PUT | `/{id}` | ✅ | Update plant |
| DELETE | `/{id}` | 🔐 | Delete plant (admin only) |

### **Cart** (`/api/cart`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ✅ | Get user's cart |
| POST | `/items` | ✅ | Add item to cart |
| PUT | `/items/{id}` | ✅ | Update item quantity |
| DELETE | `/items/{id}` | ✅ | Remove item from cart |

**Example - Add to Cart:**
```bash
POST /api/cart/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "plantId": 1,
  "quantity": 2
}
```

**Response:**
```json
{
  "id": 1,
  "status": "ACTIVE",
  "items": [
    {
      "id": 10,
      "plant": {
        "id": 1,
        "name": "Monstera Deliciosa",
        "sellingPrice": 30.0
      },
      "quantity": 2,
      "amount": 60.0
    }
  ],
  "totalAmount": 60.0
}
```

### **Orders** (`/api/orders`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ✅ | Get user's orders |
| GET | `/{id}` | ✅ | Get order details |
| POST | `/` | ✅ | Checkout (create order) |

### **Wishlist** (`/api/wishlist`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ✅ | Get user's wishlist |
| POST | `/items` | ✅ | Add to wishlist |
| DELETE | `/items/{id}` | ✅ | Remove from wishlist |

### **Plant Types** (`/api/plant-types`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ❌ | List all plant types |
| GET | `/{id}` | ❌ | Get plant type details |
| POST | `/` | ✅ | Create plant type |
| PUT | `/{id}` | ✅ | Update plant type |
| DELETE | `/{id}` | 🔐 | Delete (admin only) |

### **Growers** (`/api/growers`)

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | ❌ | List all growers |
| GET | `/{id}` | ❌ | Get grower details |
| POST | `/` | ✅ | Create grower |
| PUT | `/{id}` | ✅ | Update grower |
| DELETE | `/{id}` | 🔐 | Delete (admin only) |

**Legend**: ❌ = Public • ✅ = Authenticated User • 🔐 = Admin Only

---

## 📁 Project Structure

```
GreenThumb-main/
├── src/
│   ├── main/
│   │   ├── java/dev/mohan/greenthumb/
│   │   │   ├── bootstrap/
│   │   │   │   └── DataSeeder.java           # Pre-seed test data
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # Authentication endpoints
│   │   │   │   ├── PlantController.java      # Product endpoints
│   │   │   │   ├── CartController.java       # Shopping cart
│   │   │   │   ├── OrderController.java      # Orders
│   │   │   │   ├── WishlistController.java   # Wishlist
│   │   │   │   ├── GrowerController.java     # Suppliers
│   │   │   │   └── PlantTypeController.java  # Categories
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java          # Interface
│   │   │   │   ├── CartService.java          # Interface
│   │   │   │   ├── OrderService.java         # Interface
│   │   │   │   └── impl/                     # Implementation classes
│   │   │   │       ├── AuthServiceImpl.java
│   │   │   │       ├── CartServiceImpl.java
│   │   │   │       └── ...
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java       # User data access
│   │   │   │   ├── PlantRepository.java      # Plant queries
│   │   │   │   ├── CartRepository.java       # Cart access
│   │   │   │   ├── OrderSummaryRepository.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── Auditable.java            # Base class with timestamps
│   │   │   │   ├── User.java                 # User entity
│   │   │   │   ├── Plant.java                # Product entity
│   │   │   │   ├── Cart.java                 # Cart entity
│   │   │   │   ├── CartItem.java             # Line item in cart
│   │   │   │   ├── OrderSummary.java         # Order entity
│   │   │   │   ├── OrderItem.java            # Line item in order
│   │   │   │   ├── Wishlist.java             # Wishlist entity
│   │   │   │   ├── WishlistItem.java         # Wishlist item
│   │   │   │   ├── Role.java                 # User role
│   │   │   │   ├── Grower.java               # Supplier
│   │   │   │   └── PlantType.java            # Category
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequestDTO.java      # Login request
│   │   │   │   ├── AuthResponseDTO.java      # Auth response with token
│   │   │   │   ├── PlantDTO.java             # Plant response
│   │   │   │   ├── CartItemRequestDTO.java   # Add to cart request
│   │   │   │   ├── CartResponseDTO.java      # Cart response
│   │   │   │   ├── OrderResponseDTO.java     # Order response
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfiguration.java # Spring Security config
│   │   │   │   ├── TokenProvider.java        # JWT generation
│   │   │   │   ├── JWTFilter.java            # Token validation filter
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   │
│   │   │   ├── enumeration/
│   │   │   │   ├── CartStatus.java           # DRAFT, ACTIVE, CHECKED_OUT
│   │   │   │   ├── CartItemStatus.java       # ADDED, UPDATED, REMOVED
│   │   │   │   ├── OrderSummaryStatus.java   # PENDING, PLACED, DELIVERED
│   │   │   │   └── WishlistItemStatus.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── NotFoundException.java     # Resource not found (404)
│   │   │   │   ├── BadRequestException.java  # Invalid input (400)
│   │   │   │   └── GlobalExceptionHandler.java # Centralized error handling
│   │   │   │
│   │   │   └── GreenthumbApplication.java    # Main entry point
│   │   │
│   │   └── resources/
│   │       └── application.properties         # Configuration
│   │
│   └── test/
│       └── java/...                          # Unit and integration tests
│
├── pom.xml                                    # Maven dependencies
├── mvnw & mvnw.cmd                           # Maven wrapper
├── README.md                                  # This file
└── .gitignore                                # Git ignore rules
```

---

## 🏆 Design Patterns & Best Practices

### **1. Layered Architecture**
- **Benefit**: Separation of concerns, testability, maintainability
- **Implementation**: Controller → Service → Repository → Domain
- **File**: Each layer in separate package

### **2. Repository Pattern**
```java
// Abstraction over database queries
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByGrowerIdOrderBySortOrderAsc(Long growerId);
}
```
- **Benefit**: Easy to switch databases, mockable for testing

### **3. DTO Pattern**
```java
// API contracts separate from entities
public record CartItemRequestDTO(
    @NotNull Long plantId,
    @NotNull @Positive Integer quantity
) {}
```
- **Benefit**: Validation at boundary, never expose entities

### **4. Dependency Injection (Constructor-based)**
```java
@Service
public class CartServiceImpl {
    private final CartRepository cartRepository;
    private final PlantRepository plantRepository;
    
    public CartServiceImpl(CartRepository cartRepository, 
                         PlantRepository plantRepository) {
        this.cartRepository = cartRepository;
        this.plantRepository = plantRepository;
    }
}
```
- **Benefit**: Explicit dependencies, testable, no NullPointerException

### **5. Global Exception Handler**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
```
- **Benefit**: Consistent error responses, no scattered try-catch

### **6. Input Validation at DTO Level**
```java
public record LoginRequestDTO(
    @NotBlank(message = "Email required") String email,
    @NotBlank(message = "Password required") String password
) {}
```
- **Benefit**: Validation enforced automatically with @Valid

### **7. Auditable Base Class**
```java
@MappedSuperclass
public abstract class Auditable {
    @CreatedDate private LocalDateTime createdDate;
    @LastModifiedDate private LocalDateTime lastModifiedDate;
}
```
- **Benefit**: DRY principle, automatic timestamp tracking on all entities

---

## 🗄️ Database Schema

### **Entity Relationships**

```
User (1) ──────────────────────── (Many) Cart
  │                                   │
  │                                   ├─── (Many) CartItem ──── (Many) Plant
  ├─────────────────────────────────── (1) Wishlist
  │                                       │
  │                                       └─── (Many) WishlistItem ──── (Many) Plant
  │
  └─────────────────────────────────── (Many) OrderSummary
                                             │
                                             └─── (Many) OrderItem ──── (Many) Plant

Grower (1) ──────────────────────── (Many) Plant ──────────── (Many) PlantType
```

### **Key Tables**

| Table | Columns | Purpose |
|-------|---------|---------|
| `app_user` | id, name, email, password, active | User accounts |
| `plant` | id, name, photo, originalPrice, sellingPrice, grower_id, plantType_id | Product catalog |
| `cart` | id, status, user_id | Shopping carts |
| `cart_item` | id, quantity, amount, status, cart_id, plant_id | Items in cart |
| `order_summary` | id, status, totalAmount, user_id | Orders |
| `order_item` | id, quantity, amount, order_summary_id, plant_id | Items in order |
| `wishlist` | id, user_id | User wishlists |
| `wishlist_item` | id, status, wishlist_id, plant_id | Items in wishlist |

---

## 🔐 Security

### **Authentication: JWT (JSON Web Token)**

```
User Login
    ↓
Credentials validated (Email + Password)
    ↓
JWT Token generated with user info
    ↓
Token returned to client
    ↓
Client includes token in Authorization header
    ↓
JWTFilter validates token on each request
    ↓
Request processed if valid
```

**Configuration** (`SecurityConfiguration.java`):
- ✅ CSRF disabled (stateless API)
- ✅ Session management: STATELESS
- ✅ Public endpoints: Auth, browse products
- ✅ Protected endpoints: Cart, orders, wishlist
- ✅ Admin endpoints: DELETE operations

### **Password Security**

```java
// BCrypt hashing (industry standard)
user.setPassword(passwordEncoder.encode(request.password()));
```
- Never store plaintext passwords
- Auto-generates salt
- Adaptive cost factor (future-proof)

### **Authorization: Role-Based Access Control (RBAC)**

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) { ... }
```

**Roles**:
- `ROLE_USER` - Default user role, can shop
- `ROLE_ADMIN` - Can manage products, delete items

---

## ⚡ Performance Highlights

### **Query Optimization**
- Strategic use of `@ManyToOne` with `FetchType.LAZY`
- Proper `@JoinColumn` mapping
- Database indexes on frequently queried columns
- Projection queries (select only needed fields)

### **Caching Opportunities** (Future)
- Product catalog (rarely changes)
- Grower/PlantType lists
- User roles

### **Scalability Considerations**
- Stateless API (no sessions) → horizontal scaling
- Database connection pooling (HikariCP)
- DTOs reduce payload size
- Pagination-ready endpoints

---

## 🔮 Future Enhancements

### **Phase 1: Core Features**
- [ ] Email verification for new accounts
- [ ] Password reset flow
- [ ] Product search and filtering
- [ ] Advanced sorting and pagination

### **Phase 2: Business Features**
- [ ] Payment gateway integration (Stripe)
- [ ] Order cancellation and refunds
- [ ] Product reviews and ratings
- [ ] Inventory management
- [ ] Admin dashboard

### **Phase 3: Performance & Scaling**
- [ ] Redis caching layer
- [ ] Elasticsearch for product search
- [ ] Message queue (RabbitMQ/Kafka)
- [ ] API versioning
- [ ] Rate limiting

### **Phase 4: Enterprise Features**
- [ ] Microservices architecture
- [ ] Event-driven architecture
- [ ] Distributed tracing
- [ ] Advanced security (2FA, OAuth2)

---

## 📊 Metrics & Testing

### **Code Quality**
- **Target Coverage**: 80%+ unit test coverage
- **Architecture**: Layered, decoupled, testable
- **Error Handling**: Global exception handler
- **Validation**: Input validation at DTOs

### **Performance**
- **API Response**: <200ms average
- **Database**: Optimized queries with indexes
- **Scalability**: Stateless (horizontal scaling ready)

---

## 🤝 Contributing

Contributions welcome! Areas for improvement:
- Add unit tests (JUnit 5 + Mockito)
- Implement logging (SLF4J/Logback)
- Add API documentation (Swagger/OpenAPI)
- Optimize database queries
- Add pagination to list endpoints

---



## 👨‍💻 Author

**Mohan Krishna Thiriveedhi**

**Backend Engineer | Spring Boot Specialist | Full-Stack Developer**

- 📧 **Email**: mohankrishnathiriveedhi27@gmail.com
- 📱 **Phone**: +1 404-819-5786
- 💼 **LinkedIn**: https://www.linkedin.com/in/mohan-krishna-thiriveedhi-335255214/


**Experience**:
- 3+ years building backend services at TCS and Peoplehum
- MS in Computer Science (4.0 GPA) from Kennesaw State University
- Spring Boot expert with focus on microservices and REST APIs
- 1st Place: Capgemini AI Hackathon 2026
- 2998 Global Rank: TCS Codevita Season 10

---

## 📞 Get in Touch

**Actively looking for**: Backend Engineer / Spring Boot Developer roles

**Open to**: 
- Full-time positions
- Remote opportunities
- Relocation

Let's build something great together! 🚀

---
