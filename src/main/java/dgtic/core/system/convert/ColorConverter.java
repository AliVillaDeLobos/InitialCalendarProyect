package dgtic.core.system.convert;

import dgtic.core.system.model.enums.Color;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {

//  Esto para que en la DB se guarde el codgio del color y no la palbra.
//          Se recupera con Color.Rojo.getCodigo()

    @Override
    public String convertToDatabaseColumn(Color color) {
        return (color != null) ? color.getCodigo() : null;
    }

    @Override
    public Color convertToEntityAttribute(String codigo) {
        if (codigo == null) {
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
