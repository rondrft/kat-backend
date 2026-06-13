package com.kat.backend.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean dbUp = false;
        try (Connection conn = dataSource.getConnection()) {
            dbUp = conn.isValid(2);
        } catch (Exception e) {
            // db is down
        }
        String status = dbUp ? "UP" : "DOWN";
        int httpStatus = dbUp ? 200 : 503;
        return ResponseEntity.status(httpStatus).body(Map.of(
                "status", status,
                "database", dbUp ? "UP" : "DOWN",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
