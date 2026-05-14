package dgtic.core.system.convert;

import org.springframework.core.convert.converter.Converter;
import dgtic.core.system.model.enums.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMvcConverter implements Converter<String, Color> {

    @Override
    public Color convert(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return null;
        }
        for (Color c : Color.values()) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Código de color desconocido: " + codigo);
    }
}