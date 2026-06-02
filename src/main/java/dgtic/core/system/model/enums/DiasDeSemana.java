package dgtic.core.system.model.enums;

import java.time.DayOfWeek;
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

    public static DiasDeSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek){
            case MONDAY -> LUNES;
            case TUESDAY -> MARTES;
            case WEDNESDAY -> MIERCOLES;
            case THURSDAY -> JUEVES;
            case FRIDAY -> VIERNES;
            case SATURDAY -> SABADO;
            case SUNDAY -> DOMINGO;
        };
    }
}
