# 🎟️ Ticket Booking API

A Spring Boot-based REST API for managing user registration and deletion in a ticket booking system.

This application supports full user lifecycle operations with validation, UUID-based user IDs, and database integration. It also includes a mock service (WireMock) to simulate external dependencies.

---

## 🚀 Features

- User registration with validation
- Duplicate user prevention
- Fetch all users / individual user
- Delete user by ID
- RESTful API design with proper status codes
- WireMock support for mock services
- MySQL database integration (configurable)
- Postman collection for testing

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Gradle
- MySQL / H2 (configurable)
- REST APIs
- WireMock (Standalone Server)
- JUnit 5
- Postman

---

## 📦 Project Structure

```
📁 src/
┣ 📂 main/
┃ ┣ 📂 java/com/ratnakar/practice/TicketBookingAPI/
┃ ┃ ┣ 🎮 controller/ # REST controllers (API endpoints)
┃ ┃ ┣ ⚙️ service/ # Business logic layer
┃ ┃ ┣ 🧩 model/ # Entities and DTOs
┃ ┃ ┣ 🗃️ repository/ # JPA repositories for DB access
┃ ┃ ┣ 🚨 exception/ # Custom exceptions & global handlers
┃ ┃ ┗ 🏁 setup/ # App setup, configuration & response handling
┃ ┗ 📂 resources/
┃ ┗ 📝 application.properties # Main application configuration
┣ 📂 test/
┃ ┣ 📂 java/com/ratnakar/practice/TicketBookingAPI/
┃ ┃ ┣ 🧪 controller/ # Controller test cases (JUnit + Mockito)
┃ ┃ ┣ 🧠 service/ # Service layer test cases
┃ ┃ ┗ 🧰 setup/ # Test setup & mocks
┃ ┗ 📂 resources/
┃ ┗ 🧾 application-test.properties # Test environment config

```

---

## 🧰 Prerequisites

- Java 17+
- Gradle
- MySQL (optional, if not using H2)
- Postman

---

## ⚙️ Configuration

Update `src/main/resources/application.properties` to point to your desired DB:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticketbookingapi
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
server.port=8091

```
## 🏗️ Build & Run
./gradlew clean build
./gradlew bootRun

📬 API Endpoints
👤 User Management
### User Management Endpoints
| Method   | Endpoint                 | Description              |
| -------- | ------------------------ | ------------------------ |
| `POST`   | `/api/users/register`    | Register a new user      |
| `GET`    | `/api/users`             | Get all registered users |
| `GET`    | `/api/users/{id}`        | Get user by ID           |
| `DELETE` | `/api/users/delete/{id}` | Delete user by ID        |

📦 Mock Service (WireMock)
| Port   | Example Endpoint                 |
| ------ | -------------------------------- |
| `8092` | `http://localhost:8092/api/mock` |

📮 Sample Request: Register User
POST /api/users/register
Sample Request:
                  {
                  "firstName": "John",
                  "lastName": "Smith",
                  "email": "john.smith@example.com",
                  "mobile": "9876543210",
                  "userName": "johnsmith123",
                  "password": "john@1234"
                  }
Sample Response:
                  {
                  "msg": "New User Added Successfully",
                  "userID": "generated-uuid",
                  "firstName": "John",
                  "lastName": "Smith",
                  "userName": "johnsmith123"
                  }

🧪 Testing the APIs
✅ Postman Collection
Located at: TicketBookingApiPostmanCollection/TicketBookingAPI.postman_collection.json

📝 Notes
Ensure ports 8091 (main app) and 8092 (mock service) are free.

Use MySQL or switch to H2 by uncommenting relevant lines in application.properties.

UUID is used for primary key user_id.

📌 Future Enhancements
1) JWT-based authentication
2) Booking service integration
3) Swagger/OpenAPI documentation
4) Docker containerization

👨‍💻 Author
Ratnakar Kolhatkar
🔗 GitHub
