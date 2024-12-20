# Server Exercise

## Project Summary

This project involved developing an HTTP server for managing a book inventory system. The server implements CRUD (Create, Read, Update, Delete) functionality and provides support for advanced filtering and validations.

## Implementation Details

1. **Framework Used:**
   - Developed using Java with frameworks like Spark Java or Jetty for server functionalities.

2. **Endpoints Implemented:**
   - **Health Check**: Verifies server availability.
   - **CRUD Operations**: Supports operations on books (Add, Update, Retrieve, Delete).
   - **Filtering**: Filters based on multiple criteria like author, year, price range, and genre.

3. **Response Structure:**
   - Unified JSON response for all endpoints with a clear structure for success and error messages.

4. **Data Validation:**
   - Ensured unique book titles.
   - Validated year and price constraints.

5. **Advanced Features:**
   - Case-insensitive matching for authors and genres.
   - Support for stable sorting of results.

## Features Implemented

- Full support for book inventory management.
- Advanced filtering and sorting.
- Robust error handling and validation.

## Testing

Performed extensive testing using Postman and curl to ensure proper endpoint functionality, error handling, and adherence to JSON response standards.

## Project Setup

### Prerequisites
- Java SDK installed.
- Dependencies for JSON parsing (e.g., Gson or Jackson).

### Accessing the Server
Server runs on `localhost:8574`. Use tools like Postman to test endpoints.

### Run:
- Run the run.bat file.
- Using the supplied Postman collection, postman environment that I give you.

