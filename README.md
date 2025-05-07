# ChatterBox Follower Service

A Spring Boot microservice responsible for managing user follow/unfollow relationships on the ChatterBox platform. It integrates with Redis for efficient data storage and Kafka for propagating follow events to other microservices. Built with Java 21 and Spring Boot 3.2.4.

---

## 🚀 Features

* **Follow/Unfollow Functionality**: Users can follow or unfollow other users.
* **Redis-backed Data Storage**: Followers and following lists are stored using Redis sets.
* **Kafka Integration**: Each follow event is sent to a Kafka topic (`chatterbox-follow-events`).
* **User Validation**: Validates users against a remote User Service using Reactor Netty.
* **Global Exception Handling**: Standardized JSON error responses for expected and unexpected errors.
* **Debug Endpoint**: Optional endpoint to list all Redis keys and values for dev/debugging.

---

## 🛠️ Technology Stack

* Java 21
* Spring Boot 3.2.4
* Redis (via Spring Data Redis)
* Apache Kafka (via Spring Kafka)
* Reactor Netty (non-blocking HTTP client)
* Lombok

---

## 📁 Project Structure

```
com.chatterbox.followerservice
├── controller         # REST controllers (follow/unfollow APIs)
├── service            # Business logic (Redis storage, validation)
├── connector          # Reactive HTTP client to call User Service
├── repository         # Redis data access layer
├── kafka              # Kafka producer for follow events
├── config             # Redis configuration bean
├── exception          # Custom exceptions and global error handling
├── model              # Data classes (FollowRequest, User)
```

---

## ⚙️ Configuration

### `application.yml`

```yaml
server:
  port: 9095

spring:
  application:
    name: chatterbox-follower-service
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    follow-events-topic-name: chatterbox-follow-events
  redis:
    host: localhost
    port: 6379
    timeout: 60000

follower-service:
  connector:
    user-service:
      get-by-username-endpoint: http://localhost:9091/api/users/username/
```

---

## 🔌 Endpoints

### Follow/Unfollow

* `POST /api/follower/follow` — Body: `{ followerUsername, followeeUsername }`
* `POST /api/follower/unfollow` — Body: `{ followerUsername, followeeUsername }`

### Fetch Follow Data

* `GET /api/follower/followers/{userId}` — Returns list of followers
* `GET /api/follower/following/{userId}` — Returns list of following

### Debug

* `GET /api/follower/debug/redis-keys` — Prints all Redis keys and values (dev only)

---

## 🧪 Running Locally

1. Ensure Redis and Kafka are running on your machine:

   ```bash
   brew services start redis
   # Start Kafka (if using local setup)
   ```

2. Run the application:

   ```bash
   mvn spring-boot:run
   ```

3. Test with Postman or curl:

   ```bash
   curl -X POST http://localhost:9095/api/follower/follow \
     -H "Content-Type: application/json" \
     -d '{"followerUsername": "user1", "followeeUsername": "user2"}'
   ```

---

## ⚠️ Notes

* Ensure the User Service is running at the configured `get-by-username-endpoint`.
* `GET /debug/redis-keys` should be secured or disabled in production.

---

## 📦 Build

```bash
mvn clean package
```

---

## 📜 License

This project is part of the ChatterBox platform (internal use only).

---

## 🤝 Contributing

1. Fork the repo
2. Create a new branch
3. Submit a PR with a clear description

PRs for unit tests, logging improvements, or refactoring are welcome!
