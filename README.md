# Cognyte - Event Management Application

This application was built to manage events, providing features to create, edit, list, and delete, developed with Java + Spring Boot for the backend and React + TypeScript + Vite for the frontend.

## Technologies Used

- **Backend**: Java with Spring Boot (Spring Data JPA, Spring Web, PostgreSQL Driver and Lombok).
- **Frontend**: ReactJS + TypeScript + Vite with Redux Toolkit, Axios.
- **Styling**: Ant Design + Styled Components
- **Database**: PostgreSQL

## Requirements

Before setting up the application, ensure your environment meets the following requirements:

### Backend
- **Java**: OpenJDK 21  
  Install via your package manager or download from the [OpenJDK website](https://openjdk.org/).  
  
- **Maven**: Apache Maven 3.9+  
  Ensure Maven is installed for dependency management and building.
- **Database**: PostgreSQL 16.2
  Make sure PostgreSQL is installed and running, or use it from the Docker container.  

### Frontend
- **Node.js**: Node.js 22.17.1  
  Install Node.js 22 using a version manager like `nvm` or directly from the [Node.js website](https://nodejs.org/).  
 
- **yarn**: Install yarn directly from the [yarn website](https://classic.yarnpkg.com/lang/en/docs/install/) or using the `npm` package installer with the following command:
   ```bash 
   npm install -g yarn
   ```

## Run with Docker
You can use Docker to quickly set up and run the application. Follow these steps:
### Running all together:
1. Clone the repository
2. Go to the folder `cognyte-event-manager` you've just downloaded and run the following command:
   ```bash
   docker-compose up --build -d
   ```
### Running the Backend

1. Go to the folder `backend` and run the following command:
   ```bash
   docker-compose up --build -d
   ```
   this command will build and start both Backend and PostgreSQL containers.

   - The backend will be available at [http://localhost:8080](http://localhost:8080).
   - The database will be running on port `5432`.
 - You can find the Insomnia Collection file for this project on the folder `backend/local/docker/Insomnia_2025-07-22.yaml` and import it to your Insomnia application to test the API.
 - You can download Insomnia REST Client directly from it's web site: https://insomnia.rest/download

### Frontend

1. Go to the folder `frontend` and run the following command:
   ```bash
   docker-compose up --build -d
   ```
   this command will build and start the Frontend container of the application.

   - The frontend will be available at [http://localhost:4173](http://localhost:4173).

## Local Development Setup
If you prefer to set up the application manually for local development, follow these steps:

### Backend
**Important**: You should have a configured and running PostgreSQL instance.

1. Clone the repository.
2. Go to the `backend` directory and install dependencies using the following command:
   ```bash
   ./mvnw clean install
3. Configure the database connection in application.properties:
    ```java
    spring.datasource.url=jdbc:postgresql://localhost:5432/event-manager-db
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    ```
4. Run the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```
5. The backend will be available at http://localhost:8080.

You can also have this project imported and configured in your prefered working tool, like Intellij IDEA or VSCode.

### Frontend
1. Clone the repository.
2. Navigate to the `frontend` directory and install dependencies:
   ```bash
   yarn install
   ```
3. Start the application with the following command:
    ```bash
   yarn run dev
   ```
4. The frontend will be available at http://localhost:5173.
