# Algebraic Equation Solver API

A Spring Boot REST application that manages and evaluates algebraic equations using a postfix tree (expression tree) implementation.

## Overview

This application provides a RESTful API for:
- Storing algebraic equations in a postfix tree structure
- Retrieving stored equations
- Evaluating equations by substituting variable values

## Technical Stack

- Java 17
- Spring Boot 3.x
- Maven
- JUnit 5
- Spring Test

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher
- Your favorite IDE (IntelliJ IDEA recommended)

### Building the Application

```bash
# Clone the repository
git clone [repository-url]

# Navigate to project directory
cd algebraic-equation-solver

# Build the project
mvn clean install
```

### Running the Application

```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR file directly
java -jar target/algebraic-equation-solver-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Documentation

### 1. Store an Equation

Stores a new algebraic equation in the system.

**Endpoint:** `POST /api/equations/store`

**Request Body:**
```json
{
    "equation": "3x + 2y - z"
}
```

**Response:**
```json
{
    "message": "Equation stored successfully",
    "equationId": "1"
}
```

### 2. Retrieve All Equations

Fetches all stored equations.

**Endpoint:** `GET /api/equations`

**Response:**
```json
{
    "equations": [
        {
            "equationId": "1",
            "equation": "3x + 2y - z"
        },
        {
            "equationId": "2",
            "equation": "x^2 + y^2 - 4"
        }
    ]
}
```

### 3. Evaluate an Equation

Evaluates a specific equation with provided variable values.

**Endpoint:** `POST /api/equations/{equationId}/evaluate`

**Request Body:**
```json
{
    "variables": {
        "x": 2,
        "y": 3,
        "z": 1
    }
}
```

**Response:**
```json
{
    "equationId": "1",
    "equation": "3x + 2y - z",
    "variables": {
        "x": 2,
        "y": 3,
        "z": 1
    },
    "result": 10
}
```

## Error Handling

The API implements comprehensive error handling for various scenarios:

- Invalid equation syntax
- Missing variables during evaluation
- Non-existent equation ID
- Invalid mathematical operations

Error responses follow this format:
```json
{
    "message": "Detailed error message",
    "timestamp": "2024-12-19T10:30:45Z"
}
```

## Testing

The application includes comprehensive unit and integration tests.

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

Coverage reports can be found in `target/site/jacoco/index.html`

### Testing with Postman

1. Import the provided Postman collection from `equation-service.postman_collection`
2. Start the application locally
3. Execute the requests in the collection

## Implementation Details

### Postfix Tree Implementation

The application uses a postfix (expression) tree for storing and evaluating equations:
- Operators are stored as parent nodes
- Operands are stored as child nodes
- The tree structure enables efficient equation evaluation