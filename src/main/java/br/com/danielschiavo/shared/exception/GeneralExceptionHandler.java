package br.com.danielschiavo.shared.exception;


import java.util.Map;

import br.com.danielschiavo.filestorage.exception.FileStorageException;
import br.com.danielschiavo.shared.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> tratarErro404(EntityNotFoundException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.failure(message, null));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarErro400(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Response.failure(ex.getBody().getDetail(), null));
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> fileNotFound(ValidationException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failure(message, null));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> fileStorageException(FileStorageException e) {
        String message = e.getMessage();
        Map<String, String> details = e.getDetails();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failureMap(message, details));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failure("Falha ao persistir no banco de dados", null));
    }
}
