# Altech Checkout Backend

A simple Spring Boot Java backend for managing an electronics store’s checkout system.

## Features
- Admin: Add/remove products, add deals
- Customer: Add/remove items to/from basket, generate receipt with deals
- REST API built using Spring Boot
- In-memory data store (thread-safe)
- Tested with JUnit

## Technologies
- Java 17
- Spring Boot 3
- Maven
- Docker

## Build & Run Locally
```bash
# Clone the repo
https://github.com/thanhphuc1991/altech-demo.git
cd altech-demo

# Build
mvn clean install

# Run
mvn spring-boot:run
```

## Run Tests
```bash
mvn test
```

## Run with Docker
```bash
# Build JAR
mvn clean package

# Build Docker image
docker build -t altech .

# Run container
docker run -p 8080:8080 altech
```

## API Examples
### Admin APIs
```bash
# Create a product
POST /admin/products?name=TV&price=500

# Create a deal
POST /admin/deals?productId={productId}&buyQty=1&discountQty=1&percent=50
```

### Basket APIs
```bash
# Add product to basket
POST /basket/{basketId}/add?productId={productId}

# View receipt
GET /basket/{basketId}/receipt
```
