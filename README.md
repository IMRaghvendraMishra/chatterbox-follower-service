# ChatterBox - Follower Service

The **Follower Service** is a core microservice of the ChatterBox platform. It manages the follow/unfollow actions between users and provides endpoints to fetch follower/following data. This mock implementation uses in-memory data structures and is suitable for testing and initial development.

## Features

- Follow a user
- Unfollow a user
- List followers of a user
- List users followed by a user

## Tech Stack

- Java 21
- Spring Boot 3
- RESTful API
- In-memory data store (for mock implementation)

## API Endpoints

| Method | Endpoint              | Description                                   |
|--------|-----------------------|-----------------------------------------------|
| POST   | `/follow`             | Follow a user (requires userId, targetUserId) |
| POST   | `/unfollow`           | Unfollow a user                               |
| GET    | `/followers/{userId}` | Get all followers of a user                   |
| GET    | `/following/{userId}` | Get all users followed by a user              |

## Example Request

### Follow a User

```http
POST /follow
Content-Type: application/json

{
  "userId": "user123",
  "targetUserId": "user456"
}
