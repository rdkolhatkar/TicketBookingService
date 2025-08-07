# Ticket Booking API

A Spring Boot application for ticket booking management with user registration and authentication.

## Technologies Used

- Java
- Spring Boot
- Gradle
- RESTful APIs
- Postman for API testing

## Prerequisites

- JDK 11 or later
- Gradle
- Your favorite IDE (IntelliJ IDEA recommended)
- Postman for testing APIs

## Getting Started

### Building the Application

```bash
./gradlew clean build
```

### Running the Application

```bash  
./gradlew bootRun
```

### Testing the Application

You can use Postman to test the API endpoints. Import the provided Postman collection file to get started.

### API Endpoints

- **User Registration**: `POST /api/users/register`
- **User Deletion**: `DELETE /api/users/delete/{userId}`
- **Mock Service**: `http://localhost:8092/api/mock`
- 

### Database

The application uses an in-memory H2 database for development and testing. You can switch to a different database by
modifying the `application.properties` file.

### Configuration

The application configuration is managed through the `application.properties` file. You can set your database connection
details, server port, and other configurations here.

### Testing

The application includes unit tests and integration tests. You can run the tests using the following command:

```bash
./gradlew test
```

The application will start on:
User Service: http://localhost:8091
Mock Service: http://localhost:8092
API Documentation
User Management APIs

1. Register User
    - **Endpoint**: `POST /api/users/register`
    - **Request Body**:
      ```json
      {
      "firstName": "Jhon",
      "lastName": "Smith",
      "email": "abcd@gmail.com",
      "mobile": "9604822549",
      "userName": "U1Jhon@4",
      "password": "jhonsmith@1234"
      }
      ```
    - **Response**:
      ```json
      {
         "message": "User registered successfully"
      }
      ```

2. Delete User
   Endpoint: DELETE /api/users/delete/{userId}
   Port: 8091

3. Testing
   Postman Collection
   A comprehensive Postman collection is included in the project at: TicketBookingApiPostmanCollection/TicketBookingAPI.postman_collection.json The collection includes test scenarios for:  
   Successful user registration (200)
   Bad request handling (400)
   Server error handling (500)

4. Running Tests
   Import the Postman collection and execute the requests to test different scenarios.

5. Important Notes
   Ensure both ports (8091 and 8092) are available before starting the application
   The mock service runs on port 8092 for testing purposes
   The main service runs on port 8091