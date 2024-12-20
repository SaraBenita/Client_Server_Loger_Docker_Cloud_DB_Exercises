# Logs Exercise

## Overview
The Logs Exercise is an extension of the book server project. The goal of this task was to implement a robust logging mechanism for the server, which tracks and records server events at various levels of detail. Logs were stored in a structured format and provided insights into server activity and request handling.

## Objective
The primary goal was to:
1. Implement logging capabilities that record server activities at different log levels (INFO, DEBUG, ERROR).
2. Associate each log with a unique request identifier for better traceability.
3. Provide endpoints to dynamically retrieve and set log levels.

## Key Features
### 1. **Request Logging**
   - Logs every incoming HTTP request.
   - Details include:
     - Request number (incremental counter).
     - Timestamp in the format: `dd-MM-yyyy HH:mm:ss.SSS`.
     - HTTP method (e.g., GET, POST).
     - Resource accessed (e.g., `/books`).
   - Log levels:
     - **INFO**: Logged when a request is received.
       Example:  
       `22-12-2024 10:15:30.123 INFO: Incoming request | #5 | resource: /books | HTTP Verb GET`
     - **DEBUG**: Logged after processing a request, includes the processing time.
       Example:  
       `22-12-2024 10:15:30.456 DEBUG: request #5 duration: 123ms`

### 2. **Books Logging**
   - Captures operations related to the book inventory:
     - Adding new books.
     - Retrieving book details or counts.
     - Updating or deleting books.
   - Log levels:
     - **INFO**: Describes the operation in general terms.
       Example:  
       `INFO: Creating new Book with Title [The Great Gatsby]`
     - **DEBUG**: Provides detailed insights, such as total books in the system or changes in book details.
       Example:  
       `DEBUG: Currently there are 5 Books in the system. New Book will be assigned with id 6`

   - In case of errors (e.g., invalid data), logs the error message at the **ERROR** level.

### 3. **Log Management Endpoints**
   - **GET `/logs/level`**  
     Retrieves the current logging level for a specified logger.
     Example:  
     Request:  
     ```
     GET /logs/level?logger-name=request-logger
     ```
     Response:  
     ```
     INFO
     ```
   - **PUT `/logs/level`**  
     Dynamically updates the logging level for a specified logger.
     Example:  
     Request:  
     ```
     PUT /logs/level?logger-name=request-logger&logger-level=DEBUG
     ```
     Response:  
     ```
     DEBUG
     ```

### 4. **Log File Management**
   - Logs are written to specific files within a `logs` folder:
     - `requests.log`: Stores all request-related logs.
     - `books.log`: Stores all book-related logs.
   - The folder persists after the server process ends, enabling automated tests to validate the logs.

## Implementation Details
- **Logging Framework**: Used [Log4j / java.util.logging] for implementing the logging mechanism.
- **Unique Request Tracking**: Implemented a global counter to assign a unique identifier to each incoming request.
- **Dynamic Log Level Control**: Provided APIs to adjust log levels at runtime, ensuring flexibility for debugging and monitoring.

## Challenges and Solutions
- **Synchronization of Logs**: Ensured sequential logging by assuming single-threaded request handling.
- **Structured Log Format**: Followed a predefined structure to standardize logs and make them machine-readable.
- **Error Logging**: Captured error messages gracefully and logged them appropriately without impacting normal log flow.

## Setup Instructions
1. Ensure Java is installed on your system.
2. Clone or download the project repository.
3. Run the application using the `run.bat` file included in the project.
4. Access the server via port `8574` and test endpoints using tools like Postman.
5. Check the `logs` folder for generated log files.

### Run:
- Run the run.bat file.
- Using the supplied Postman collection, postman environment that I give you.
