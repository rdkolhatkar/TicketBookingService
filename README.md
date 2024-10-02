# TicketBookingAPI

## Overview

The TicketBookingAPI project is a robust application developed using the Spring Boot framework, designed to facilitate ticket booking services. This project incorporates modern development practices and tools to ensure high performance, reliability, and effective testing.

## Technologies Used

- **Framework**: Spring Boot
- **Build Tool**: Gradle
- **Programming Language**: Java
- **BDD Automation**: Serenity with Cucumber
- **Performance Testing**: Gatling
  - **Gatling Scripts**: Written in Scala
- **API Mocking**: WireMock

## Features

- **Ticket Booking Service**: A comprehensive system for managing and processing ticket bookings.
- **Automated Testing**: BDD automation using Serenity and Cucumber for thorough and reliable test coverage.
- **Performance Testing**: Utilizes Gatling for performance testing to ensure the application meets scalability and response time requirements.
- **API Mocking**: WireMock is used to mock API calls for effective automated testing and isolation of test scenarios.

## Getting Started

### Prerequisites

- **Java**: Ensure Java Development Kit (JDK) 11 or higher is installed.
- **Gradle**: Install Gradle to build the project.
- **Scala**: Required for running Gatling performance tests.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   ```

2. **Build the Project**:
   Navigate to the project directory and run:
   ```bash
   ./gradlew build
   ```

3. **Run the Application**:
   Start the application using:
   ```bash
   ./gradlew bootRun
   ```

### Testing

1. **Run Automated Tests**:
   Execute BDD tests with:
   ```bash
   ./gradlew test
   ```

2. **Run Performance Tests**:
   Execute Gatling performance tests by running Scala scripts:
   ```bash
   ./gradlew gatlingRun
   ```

## Contributing

1. **Fork the Repository**: Create a personal copy by forking it on GitHub.
2. **Create a Branch**: Develop features or fix issues in a new branch.
3. **Submit a Pull Request**: After testing, submit a pull request for review.

## Contact

For any questions or support, please reach out to the project maintainers at [sushilyadav0606@gmail.com].

## License

This project is licensed under the [MIT License](LICENSE).

Feel free to explore and contribute to the TicketBookingAPI project to enhance its functionality and performance!
