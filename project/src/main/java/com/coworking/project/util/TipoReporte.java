package com.coworking.project.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoReporte {
   OCUPACION,
    INGRESOS,
    USO_POR_USUARIO,
    RESERVAS,
    GENERAL;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TipoReporte from(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (TipoReporte tipo : TipoReporte.values()) {
            if (tipo.name().equalsIgnoreCase(value.trim())) {
                return tipo;
            }
        }
        
        throw new IllegalArgumentException("Tipo de reporte inválido: " + value);
    }
}
