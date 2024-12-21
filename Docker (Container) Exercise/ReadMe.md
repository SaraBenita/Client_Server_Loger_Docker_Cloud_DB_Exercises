# Docker Exercise

## Summary
This task required containerizing the server developed in a previous exercise using Docker. The output was a functional Docker image that encapsulated the server application. The container was tested and validated using automated tools.

## Objectives
- Package the server application into a Docker container.
- Upload the container image to Docker Hub for external use.
- Test the container for compliance with given requirements.

## Technologies Used
- Java: Server-side logic.
- Docker: For creating and managing containers.

## Implementation Steps
1. **Environment Setup:**
   - Installed Docker and Docker Compose on the local machine.
   - Configured the application to expose its API on port 8574 internally and port 4785 externally.

2. **Dockerfile Creation:**
   - Created a `Dockerfile` specifying the base image, dependencies, and build commands.

3. **Image Creation and Testing:**
   - Built the Docker image locally and verified functionality.
   - Uploaded the image to Docker Hub.

4. **Validation:**
   - Tested the container using HTTP requests to ensure compliance with the exercise's requirements.
