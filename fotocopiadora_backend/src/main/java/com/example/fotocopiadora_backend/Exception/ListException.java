package com.example.fotocopiadora_backend.Exception;

import java.util.List;
import java.util.Map;

public class ListException extends RuntimeException {
    private final List<Map<String, String>> errores;

    public ListException(List<Map<String, String>> errores) {
        super("Errores en la lista de precios");
        this.errores = errores;
    }

    public List<Map<String, String>> getErrores() {
        return errores;
    }
}