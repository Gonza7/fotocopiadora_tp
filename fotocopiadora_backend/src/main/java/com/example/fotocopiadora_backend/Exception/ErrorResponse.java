package com.example.fotocopiadora_backend.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String mensaje;
    private LocalDateTime timestamp;
    private int status;
    private Map<String, String> fieldErrors;

    public ErrorResponse(String mensaje, HttpStatus status) {
        this.mensaje = mensaje;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String mensaje, HttpStatus status, Map<String, String> fieldErrors) {
        this.mensaje = mensaje;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
        this.fieldErrors = fieldErrors;
    }

}

