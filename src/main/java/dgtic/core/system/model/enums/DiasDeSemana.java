package dgtic.core.system.model.enums;

import java.util.Arrays;

public enum DiasDeSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    public static DiasDeSemana fromString(String nombre) {
        return Arrays.stream(DiasDeSemana.values())
                .filter(d -> d.name().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Día no válido: " + nombre));
    }
}
