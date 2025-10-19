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
    public String toJson() {
        return this.name();
    }

    @JsonCreator
    public static TipoReporte fromJson(String value) {
        return TipoReporte.valueOf(value.toUpperCase());
    }
}
