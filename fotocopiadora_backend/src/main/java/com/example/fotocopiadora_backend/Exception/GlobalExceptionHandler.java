package com.example.fotocopiadora_backend.Exception;

import com.example.fotocopiadora_backend.Exception.Usuario.ContraseñaIncorrectaException;
import com.example.fotocopiadora_backend.Exception.Usuario.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioExistente(UsuarioYaExisteException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ContraseñaIncorrectaException.class)
    public ResponseEntity<ErrorResponse> handleContraseñaIncorrecta(ContraseñaIncorrectaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        ErrorResponse error = new ErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, errores);
        return ResponseEntity.badRequest().body(error);
    }

}
