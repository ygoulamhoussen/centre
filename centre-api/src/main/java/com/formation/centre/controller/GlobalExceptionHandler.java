package com.formation.centre.controller;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Fichier trop volumineux");
        response.put("message", "La taille du fichier dépasse la limite autorisée (10MB maximum)");
        response.put("maxSize", "10MB");
        response.put("status", HttpStatus.PAYLOAD_TOO_LARGE.value());
        
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erreur interne du serveur");
        response.put("message", e.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(StaleObjectStateException.class)
    public ResponseEntity<Map<String, Object>> handleStaleObjectStateException(StaleObjectStateException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Conflit de version");
        response.put("message", "L'objet a été modifié par une autre transaction. Veuillez rafraîchir et réessayer.");
        response.put("status", HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Conflit de version");
        response.put("message", "L'objet a été modifié par une autre transaction. Veuillez rafraîchir et réessayer.");
        response.put("status", HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
} 