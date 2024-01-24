# Test-task


`The following technologies are used to build the Application:`
- ☕ **Java 17**
- 🌱 **Spring Boot**
- 🌱🛢️ **Spring Data JPA**
- 🗎 **Swagger**
- 🐬 **MySQL**
- 🐋  **Docker**
- 🌶️ **Lombok**
- ↔️ **MapStruct**
- 🛢️ **Liquibase**
### ❓ How to use
`Before running this app, ensure you have the following installed:`
- ☕ Java
- 🐋 Docker

`Follow the steps below to install:`
1. Clone the repository from GitHub and navigate to the project directory.
2. Create a `.env` file with the necessary environment variables. (See `.env-sample` for a sample.)
3. Run the following command to build and start the Docker containers:
   `docker-compose up --build`.
4. The application should now be running at `http://localhost:8081`.

### Customer Management Endpoints
| **HTTP method** | **Endpoint**      | **Function**                        |
|:----------------|:------------------|:------------------------------------|
| POST            | /api/customers    | Add a new customer to db            |
| GET             | /api/customers    | Get a list of all customers         |
| GET             | /api/customers/id | Get a specific customer by id       |
| DELETE          | /api/customers/id | Make a specific customer inactive   |
| PUT             | /api/customers/id | Update info about specific customer |