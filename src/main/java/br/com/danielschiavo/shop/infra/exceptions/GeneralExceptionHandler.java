package br.com.danielschiavo.shop.infra.exceptions;


import jakarta.persistence.EntityNotFoundException;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<SimpleMessage> tratarErro404(EntityNotFoundException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleMessage(message));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDataValidationDTO>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErrorDataValidationDTO::new).toList());
    }
    
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<SimpleMessage> fileNotFound(FileNotFoundException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleMessage(message));
    }
    
    private record ErrorDataValidationDTO(String field, String message) {
        public ErrorDataValidationDTO(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
    
    private record SimpleMessage(String message) {
    	public SimpleMessage(String message) {
    		this.message = message;
    	}
    }

}
