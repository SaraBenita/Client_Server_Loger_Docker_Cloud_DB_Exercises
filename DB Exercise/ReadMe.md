# Database Exercise

## Summary
This exercise focused on working with databases (DBs) using Object-Relational Mapping (ORM) technology. The task required integrating two types of databases: Postgres (SQL) and MongoDB (NoSQL), running as Docker containers. The goal was to connect to these databases, manipulate data, and ensure consistency between them. The application logic was previously developed and remained unchanged for this exercise. The challenge was to enable persistence in the database layer and handle Dockerized environments.

## Objectives
- Store and fetch book records from both Postgres and MongoDB.
- Maintain data consistency across the two databases.
- Ensure smooth communication between the application and the databases via Docker containers.
- Create and push a Docker image to Docker Hub.

## Technologies Used
- Java: Server-side application logic.
- Spring Boot: Backend framework for API development.
- Hibernate: ORM for interacting with Postgres.
- MongoDB Java Driver: For interacting with MongoDB.
- Docker: For containerization of the application and databases.
- Docker Compose: To manage multi-container environments.

## Implementation Steps
1. **Database Setup:**
   - Two Docker containers for Postgres and MongoDB were initialized with specific configurations.
   - Postgres and MongoDB were pre-populated with book records.
   - The application connected to these containers using predefined credentials and endpoints.

2. **Application Logic:**
   - CRUD operations were implemented to handle book records.
   - API endpoints were created to interact with both databases, ensuring identical data in both Postgres and MongoDB.

3. **Docker Integration:**
   - A `Dockerfile` was created for the application container.
   - Docker Compose was used to define and link the application and database containers.
   - The application image was uploaded to Docker Hub.

4. **Testing:**
   - Postman collection was provided for automated testing of endpoints.
   - Validations ensured the application could handle data consistency and respond correctly.
