## 📘 Project Documentation  
**Title:** FlightBooker
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
   ```bash
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
- **Frontend:** Likely React (to be confirmed by checking `frontend`)
- **Authentication:** JWT
- **Communication:** RESTful APIs
- **Database:** Not specified yet, will confirm from source
- **Containerization:** Docker, Docker Compose
- **Diagrams:** PlantUML class/sequence diagrams

---

### 🔄 **Inter-Service Communication**
- **REST APIs** are used between services (JWT for auth).
- **Kafka** is used for async communication.

---

### 🖼️ **Architecture Overview**
## Context diagram
![image](https://github.com/user-attachments/assets/071291a0-790a-4c87-b198-b59a9c91b852)

## Container diagram
![image](https://github.com/user-attachments/assets/1e63b8ce-136f-474a-8710-c5102a38ea09)

## Component diagrams

### BookingService
![image](https://github.com/user-attachments/assets/91f40d77-9ce3-4391-9e66-8ff5fe2a9bf8)

### FlightService
![image](https://github.com/user-attachments/assets/f89393d8-020d-4cfc-bd1d-3d82824e4b44)

### PaymentService
![image](https://github.com/user-attachments/assets/2f914eb1-9ccf-4a85-9f95-145cd69dc787)

### UserService
![image](https://github.com/user-attachments/assets/7c858eb7-e4e1-413d-9c78-8a86300b086b)

### NotificationService
![image](https://github.com/user-attachments/assets/d5b239f5-cc06-4ee4-8bf2-5638d26319a6)

---

### 📂 Folder Structure (Highlights)
```
├── bookingservice/
├── flightservice/
├── userservice/
├── jwtutil/
├── frontend/
├── docker-compose.yml
├── .env
├── README.md
```

---
