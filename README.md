# Pet Adoption Centre System

## Architecture & Design Patterns

### 1. MVC Architecture
- **Model**: JPA Entities representing the core business objects.
- **View**: REST API responses (JSON).
- **Controller**: REST Controllers handling HTTP requests.

---

## 🔌 API Endpoints 
 
### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Login with email, password, role |
 
### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | `/users/register` | Register a new user |
| GET | `/users` | Get all users |
| GET | `/users/volunteers` | Get all volunteers |
| PATCH | `/users/volunteers/{id}/availability` | Toggle volunteer availability |
 
### Pets
| Method | Endpoint | Description |
|---|---|---|
| GET | `/pets` | Get all available pets |
| GET | `/pets/all` | Get all pets |
| POST | `/pets/add` | Add a new pet |
| GET | `/pets/pending-review` | Get pets pending approval |
| PATCH | `/pets/{id}/approve` | Approve a pet listing |
| PATCH | `/pets/{id}/reject` | Reject a pet listing |
| PATCH | `/pets/{id}/vaccination` | Update vaccination status |
| GET | `/pets/my-registrations` | Get pets registered by a user |
 
### Appointments
| Method | Endpoint | Description |
|---|---|---|
| POST | `/appointments/book` | Book an appointment |
| GET | `/appointments/all` | Get all appointments |
| DELETE | `/appointments/{id}` | Cancel an appointment |
| PATCH | `/appointments/{id}/status` | Update appointment status |
| PATCH | `/appointments/{id}/complete` | Mark as completed |
| PATCH | `/appointments/{id}/expire` | Mark as expired |
 
### Vet Appointments
| Method | Endpoint | Description |
|---|---|---|
| POST | `/vetappointments/book` | Book a vet appointment (Breeder only) |
| GET | `/vetappointments/all` | Get all vet appointments |
| DELETE | `/vetappointments/{id}` | Cancel a vet appointment |
| POST | `/vetappointments/{id}/confirm` | Confirm appointment |
| POST | `/vetappointments/{id}/complete` | Complete appointment |
 
### Tasks
| Method | Endpoint | Description |
|---|---|---|
| GET | `/tasks` | Get all tasks |
| GET | `/tasks/open` | Get published tasks |
| POST | `/tasks/create` | Create a task |
| PATCH | `/tasks/{id}/publish` | Publish a task |
| PATCH | `/taskId}/assign/{volunteerId}` | Assign to volunteer |
| PATCH | `/tasks/{taskId}/choose/{volunteerId}` | Volunteer self-selects task |
| PATCH | `/tasks/{taskId}/start` | Start a task |
| PATCH | `/tasks/{taskId}/complete` | Mark task complete |
| PATCH | `/tasks/{taskId}/review` | Staff reviews completed task |
 
### Queries
| Method | Endpoint | Description |
|---|---|---|
| POST | `/queries/ask` | Submit a query |
| POST | `/queries/respond` | Respond to a query |
| GET | `/queries/all` | Get all queries |
 
### Adoptions
| Method | Endpoint | Description |
|---|---|---|
| POST | `/adoptions/create` | Create an adoption record |
| GET | `/adoptions/user/{id}` | Get adoptions by user |
 
### Admin
| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/data/all` | Get all system data in one response |
| GET | `/admin/data/users/all` | Get all users |
| GET | `/admin/data/pets/all` | Get all pets |
| GET | `/admin/data/appointments/all` | Get all appointments |
 ---

## 🛠 Prerequisites
- Java 17+
- Maven 3.6+

## 🚀 How to Run
1. Navigate to the project root.
2. Run `./mvnw spring-boot:run` (or `mvn spring-boot:run`).
3. Access H2 Console at: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:petdb`)
