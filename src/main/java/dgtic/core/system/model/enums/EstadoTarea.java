package dgtic.core.system.model.enums;

public enum EstadoTarea {
    PENDIENTE,
    EN_PROGRESO,
    CANCELADA,
    FINALIZADA;

    public static EstadoTarea fromString(String s) {
        for (EstadoTarea e : values()) {
            if (e.name().equalsIgnoreCase(s)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + s);
    }
}
