# E-Commerce Microservices Architecture

## Overview
This project is an **E-Commerce Application** built using **Spring Boot** and follows a **Microservices Architecture**. It consists of multiple independent services that handle user management, product listings, orders, and payments.

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Cloud
- **Database:** MySQL
- **Message Broker:** Apache Kafka
- **API Gateway:** Spring Cloud Gateway
- **Service Discovery:** Eureka
- **Authentication:** Spring Security, JWT

## Microservices Structure
| Service Name        | Description |
|---------------------|-------------|
| **User Service**    | Manages user authentication & profiles |
| **Product Service** | Handles product catalog & details |
| **Order Service**   | Manages customer orders |
| **Payment Service** | Processes payments securely |
| **Inventory Service** | Tracks product stock levels |

## User Service (Example)
The **User Service** is responsible for **user registration, authentication, and profile management**.

### Endpoints:
- `POST /api/users/register` → Register a new user
- `POST /api/users/login` → Authenticate and get JWT
- `GET /api/users/profile/{id}` → Get user profile

## Setup & Installation
### 1. Clone the Repository
```bash
git clone https://github.com/irfanakbar367/E-Commerce-Using-Microservices-Architecture.git
cd E-Commerce-Using-Microservices-Architecture
```

### 2. Start Services Locally
Ensure **Java 17+, MySQL/PostgreSQL, and Docker** are installed.
```bash
mvn clean install
docker-compose up -d
```

### 3. Run Microservices
Each service must be started individually:
```bash
cd UserService && mvn spring-boot:run
```

## API Gateway & Service Discovery
This project uses **Spring Cloud Gateway** to route requests and **Eureka** for service discovery.
- Eureka Dashboard: `http://localhost:8761`
- API Gateway: `http://localhost:8080`

## Future Enhancements
- Implementing a **Recommendation System**
- Adding **Deployments**

## License
This project is open-source and available under the **MIT License**.
