package com.example.fotocopiadora_backend.Exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}
