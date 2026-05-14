package dgtic.core.system.convert.DiasSemanaConverter;

import dgtic.core.system.model.enums.EstadoTarea;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EstadoTareaMvcConverter implements Converter<String, EstadoTarea> {

    @Override
    public EstadoTarea convert(String data) {
        if (data == null || data.trim().isEmpty()) {
            return null;
        }
        // Usa tu método fromString para manejar 'pendiente' o 'PENDIENTE'
        return EstadoTarea.fromString(data);
    }
}
