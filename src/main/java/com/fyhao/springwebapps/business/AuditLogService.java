package com.fyhao.springwebapps.business;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Optional Firebase Realtime Database audit sink configured entirely by environment variables. */
@Service
public class AuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);
    private final RestTemplate restTemplate;

    public AuditLogService() { this(new RestTemplate()); }
    AuditLogService(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    @Async
    public void recordPdfGenerated(String operation) {
        String databaseUrl = System.getenv("FIREBASE_DATABASE_URL");
        if (databaseUrl == null || databaseUrl.trim().isEmpty()) return;
        String token = System.getenv("FIREBASE_DATABASE_AUTH");
        String endpoint = databaseUrl.replaceAll("/+$", "") + "/auditLogs.json";
        if (token != null && !token.isEmpty()) endpoint += "?auth=" + token;
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("operation", operation);
        event.put("status", "success");
        event.put("timestamp", Instant.now().toString());
        try {
            restTemplate.postForEntity(endpoint, event, Map.class);
        } catch (RuntimeException e) {
            logger.warn("Firebase audit delivery failed: {}", e.getMessage());
        }
    }
}
