package dgtic.core.system.convert.DiasSemanaConverter;

import dgtic.core.system.model.enums.EstadoTarea;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoTareaConverter implements AttributeConverter<EstadoTarea, String> {

    @Override
    public String convertToDatabaseColumn(EstadoTarea estado) {
        if (estado == null) return null;
        return estado.name().toUpperCase(); // guarda en mayusculas
    }

    @Override
    public EstadoTarea convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return EstadoTarea.fromString(dbData); // Usa el metodo que hice en el enum, para evitar errores
    }
}