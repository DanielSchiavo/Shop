package br.com.danielschiavo.shared.exception;


import java.util.List;

import br.com.danielschiavo.shared.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> tratarErro404(EntityNotFoundException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDataValidationDTO>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErrorDataValidationDTO::new).toList());
    }
    
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> fileNotFound(ValidacaoException ex) {
    	String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolation(DataIntegrityViolationException e) {
        String message = e.getRootCause().getMessage();
        String reason = null;
        String mensagem = null;
        if (e.getMessage().contains("clientes_cpf_key")) {
            mensagem = "CPF já cadastrado";
            reason = "clientes_cpf_key";
        }
        if (e.getMessage().contains("clientes_email_key")) {
            mensagem = "E-Mail já cadastrado";
            reason = "clientes_email_key";
        }
        if (e.getMessage().contains("clientes_celular_key")) {
            mensagem = "Celular já cadastrado";
            reason = "clientes_celular_key";
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Response.failure("Falha ao persistir no banco de dados", List.of(reason)));
    }
    
    private record ErrorDataValidationDTO(String field, String message) {
        public ErrorDataValidationDTO(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
