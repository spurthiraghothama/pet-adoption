# Pet Adoption Centre System - Backend

This is a complete Spring Boot backend application for a Pet Adoption Centre.

## 🏗 Architecture & Design Patterns

### 1. MVC Architecture
- **Model**: JPA Entities representing the core business objects.
- **View**: REST API responses (JSON).
- **Controller**: REST Controllers handling HTTP requests.

### 2. Creational Design Patterns
- **Factory Pattern**: Implemented in `UserFactory` to create specialized user types (`Adopter`, `Volunteer`, `ShelterStaff`, `Breeder`).
- **Singleton Pattern**: Implemented in `PetRegistry` as a thread-safe registry for managing pets centrally.
- **Builder Pattern**: Implemented in `Pet` and `Appointment` models using Lombok's `@Builder` for clean object creation.

---

## 🔌 API Endpoints & Sample Data

### 1. User Registration
`POST /users/register`
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "userType": "ADOPTER"
}
```

### 2. Add a Pet (Shelter Staff)
`POST /pets/add`
```json
{
  "name": "Buddy",
  "species": "Dog",
  "age": 2,
  "vaccinationStatus": true,
  "availabilityStatus": "AVAILABLE"
}
```

### 3. View Available Pets (Adopter)
`GET /pets`

### 4. Book Appointment (Adopter)
`POST /appointments/book`
```json
{
  "petId": 1,
  "userId": 1,
  "date": "2024-05-20",
  "time": "10:30:00"
}
```

### 5. Cancel Appointment
`DELETE /appointments/cancel/1`

### 6. Ask Query (Adopter)
`POST /queries/ask`
```json
{
  "userId": 1,
  "text": "Is Buddy friendly with children?"
}
```

### 7. Respond to Query (Shelter Staff)
`POST /queries/respond?questionId=1&answer=Yes, he is very friendly!`

---

## 🛠 Prerequisites
- Java 17+
- Maven 3.6+

## 🚀 How to Run
1. Navigate to the project root.
2. Run `./mvnw spring-boot:run` (or `mvn spring-boot:run`).
3. Access H2 Console at: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:petdb`)
