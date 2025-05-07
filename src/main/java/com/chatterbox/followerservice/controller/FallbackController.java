package com.chatterbox.followerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * FallbackController handles requests to undefined or invalid endpoints within the application.
 *
 * This controller acts as a catch-all fallback for any request that does not match a defined route.
 * It returns a standardized 404 Not Found response with details such as timestamp, status code,
 * error type, and the requested invalid URI for better traceability and debugging.
 *
 * This is especially useful in REST APIs to provide meaningful error messages to clients when they
 * hit incorrect URLs or routes that don't exist.
 *
 * Example:
 *   Request: GET /api/invalid/path
 *   Response: {
 *       "timestamp": "...",
 *       "status": 404,
 *       "error": "Not Found",
 *       "message": "Invalid endpoint: /api/invalid/path"
 *   }
 */
@RestController
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<Map<String, Object>> handleInvalidEndpoint(HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", "Invalid endpoint: " + request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
