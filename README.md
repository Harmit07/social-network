# Social Network

A full-stack social networking platform featuring user profiles, a dynamic post feed, post interactions (likes & comments), friend management, and real-time chat. 

This repository contains both the **Spring Boot Backend** and the **Frontend UI**.

## 🚀 Technologies Used
* **Backend:** Spring Boot (Java 21), PostgreSQL, Spring Security & JWT, WebSockets (STOMP)
* **Frontend:** Vite, Tailwind CSS (located in `src/main/Frontend/social-network-ui`)
* **Utilities:** Lombok, Maven, Node.js, npm

## 🛠 Prerequisites
Before running the application, ensure you have the following installed:
1. **Java 21** or higher.
2. **Node.js** and npm.
3. **PostgreSQL** running locally on port `5433`.
4. A created database named `social_network_db`.

**Database Configuration** (defined in `src/main/resources/application.properties`):
* URL: `jdbc:postgresql://localhost:5433/social_network_db`
* Username: `postgres`
* Password: `0000`

## 🏃‍♂️ How to Run

You will need to run the backend and frontend simultaneously in two separate terminals.

### 1. Run the Backend (Spring Boot)
1. Open a terminal in the project root folder.
2. Run the following Maven command:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The backend server will start on `http://localhost:8080`.

### 2. Run the Frontend (Vite)
1. Open a new terminal and navigate to the frontend folder:
   ```bash
   cd src/main/Frontend/social-network-ui
   ```
2. Install the dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. The frontend will start on `http://localhost:5173`.

## 📚 Backend API Endpoints Overview

*All endpoints (except `/api/users/register` and `/api/users/login`) require an `Authorization` header with the JWT token: `Authorization: Bearer <your_jwt_token>`*

### Authentication & Profiles
* `POST /api/users/register` - Register a new user.
* `POST /api/users/login` - Authenticate and receive a JWT token.
* `GET /api/users/profile` - Get logged-in user profile.
* `PUT /api/users/profile` - Update bio and picture.
* `GET /api/users/{username}` - View user profile.

### Posts & Interactions
* `POST /api/posts` - Create post.
* `GET /api/posts` - Get the chronological feed.
* `POST /api/posts/{postId}/like` - Like a post.
* `DELETE /api/posts/{postId}/like` - Unlike a post.
* `POST /api/posts/{postId}/comments` - Add a comment.
* `GET /api/posts/{postId}/comments` - Get comments.

### Friend Management
* `POST /api/friends/request` - Send friend request.
* `GET /api/friends/pending` - View incoming requests.
* `PUT /api/friends/accept/{connectionId}` - Accept request.
* `DELETE /api/friends/reject/{connectionId}` - Reject request.
* `GET /api/friends` - List all accepted friends.
* `DELETE /api/friends/remove/{connectionId}` - Remove friend.

### Real-time Chat
* Uses WebSockets over STOMP on the backend configuration.
