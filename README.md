# Social Network Backend API

A fully-featured RESTful API backend for a social networking platform, built with Spring Boot and Java 21. It provides features like user authentication via JWT, user profiles, a friend management system, a dynamic post feed, post interactions (likes & comments), and real-time chat via WebSockets.

## 🚀 Technologies Used
* **Framework:** Spring Boot (WebMVC, Data JPA, Security, WebSockets)
* **Language:** Java 21
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Security:** Spring Security & JWT (JSON Web Tokens)
* **Real-time:** WebSockets (STOMP)
* **Utilities:** Lombok, Maven

## 🛠 Prerequisites
Before running the application, ensure you have the following installed:
1. **Java 21** or higher.
2. **PostgreSQL** running locally on port `5433`.
3. A created database named `social_network_db`.

**Database Configuration** (defined in `application.properties`):
* URL: `jdbc:postgresql://localhost:5433/social_network_db`
* Username: `postgres`
* Password: `0000`

*(If your PostgreSQL runs on the default port `5432` or uses a different password, make sure to update `src/main/resources/application.properties`!)*

## 🏃‍♂️ How to Run
1. Clone the repository.
2. Open a terminal in the project root folder.
3. Run the following Maven command to start the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The backend server will start on `http://localhost:8080`.

## 📚 API Endpoints Overview

All endpoints (except `/api/users/register` and `/api/users/login`) require an `Authorization` header with the JWT token: 
`Authorization: Bearer <your_jwt_token>`

### Authentication
* `POST /api/users/register` - Register a new user.
* `POST /api/users/login` - Authenticate and receive a JWT token.

### User Profiles
* `GET /api/users/profile` - Get the logged-in user's profile details.
* `PUT /api/users/profile` - Update bio and profile picture URL.
* `GET /api/users/{username}` - View another user's profile.

### Posts & Interactions
* `POST /api/posts` - Create a new post.
* `GET /api/posts` - Get the chronological feed (posts from the user and their accepted friends).
* `POST /api/posts/{postId}/like` - Like a post.
* `DELETE /api/posts/{postId}/like` - Unlike a post.
* `POST /api/posts/{postId}/comments` - Add a comment to a post.
* `GET /api/posts/{postId}/comments` - Get all comments for a post.

### Friend Management
* `POST /api/friends/request` - Send a friend request.
* `GET /api/friends/pending` - View incoming pending friend requests.
* `PUT /api/friends/accept/{connectionId}` - Accept a friend request.
* `DELETE /api/friends/reject/{connectionId}` - Reject a friend request.
* `GET /api/friends` - List all accepted friends.
* `DELETE /api/friends/remove/{connectionId}` - Remove a user from your friends list.

### Real-time Chat
* Uses WebSockets over STOMP. Connect to the WebSocket endpoint defined in `WebSocketConfig.java` and subscribe to `/topic/public` for global chat broadcasts.
