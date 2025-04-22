# __FlightBooker__
### A scalable microservices-based travel booking application
---
## How to Run

### Using Gradle (for local development)
1. **Build the Project**:
   ```bash
   ./gradlew clean build

2. **Run docker compose**:
   ```bash
   docker-compose up -d

3. **Access frontend**:
   Open a browser and go to localhost:3000



---

### Microservices Overview

| Service               | Description                                                                                             |
|-----------------------|---------------------------------------------------------------------------------------------------------|
| `userservice`         | Manages user registration, authentication, and roles.                                                 |
| `flightservice`       | Stores and manages flight data (source, destination, etc.).                                             |
| `bookingservice`      | Handles booking operations and integrates payment logic.                                              |
| `notificationservice` | Sends notifications to users (via email, SMS, etc.) when bookings or payments change status.            |
| `jwtutil`             | Shared JWT-based authentication and utility functionality.                                            |
| `frontend`            | Web-based user interface for accessing the services.                                               
---

### ⚙️ **Tech Stack**
- **Backend:** Java (Spring Boot), Gradle
- **Frontend:** React
- **Authentication:** JWT
- **Communication:** RESTful APIs
- **Database:** PostgreSQL
- **Containerization:** Docker, Docker Compose
- **Diagrams:** PlantUML class/sequence diagrams

---

### 🔄 **Inter-Service Communication**
- **REST APIs** are used for frontend-service communication (JWT for auth).
- **Kafka** is used for async communication between services (using Saga Pattern).

---

### 🖼️ **Architecture Overview**
## Context diagram
![image](https://github.com/user-attachments/assets/442772ff-d931-4f12-934e-74d80a74a817)

## Container diagram
![image](https://github.com/user-attachments/assets/a58fc240-ced7-4726-a324-c8056a380884)

---
