# HTTP Client Exercise

This project demonstrates interaction with external HTTP services using Java's HTTP client libraries. The application performs a sequence of HTTP requests to a predefined server, showcasing how to handle different HTTP methods and responses.

## Features

- **HTTP Methods**:
  - Supports GET, POST, PUT, and DELETE.
  - Demonstrates sending data via query parameters and JSON body.

- **Sequential Workflow**:
  - Responses from previous requests are used as input for subsequent requests.

- **Validation**:
  - Handles invalid inputs and server responses.

## Technologies Used

- **Java**
- HTTP Client Library: Java's built-in HttpClient or Apache HttpClient
- JSON Processing: Jackson or Gson

## Workflow

1. **GET Request**
   - Endpoint: `/test_get_method`
   - Query Parameters:
     - `id`: Your 9-digit ID.
     - `year`: Your 4-digit birth year.
   - Response: String used for the next request.

2. **POST Request**
   - Endpoint: `/test_post_method`
   - Request Body:
     ```json
     {
       "id": 123456789,
       "year": 2022,
       "requestId": "<previous GET response>"
     }
     ```
   - Response: JSON object with a `message` field used for the next request.

3. **PUT Request**
   - Endpoint: `/test_put_method`
   - Query Parameter:
     - `id`: Value of the `message` field from the POST response.
   - Request Body:
     ```json
     {
       "id": (id - 123503) % 92,
       "year": (year + 123) % 45
     }
     ```
   - Response: JSON object with a `message` field used for the DELETE request.

4. **DELETE Request**
   - Endpoint: `/test_delete_method`
   - Query Parameter:
     - `id`: Value of the `message` field from the PUT response.
   - Response: None.

## Setup and Execution

1. Compile the project:
   ```bash
   javac -cp .:json-library.jar Main.java
   ```

2. Run the application:
   ```bash
   java -cp .:json-library.jar Main
   ```

3. Ensure the target server is running on `localhost:6767`.

## Testing
- Use the provided dummy server for local testing.
- Verify that all requests are performed in the correct order and responses match expectations.
-Use Postman.

