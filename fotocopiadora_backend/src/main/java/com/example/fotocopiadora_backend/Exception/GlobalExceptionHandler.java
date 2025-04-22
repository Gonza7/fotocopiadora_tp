package com.example.fotocopiadora_backend.Exception;

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

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
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
