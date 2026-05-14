package dgtic.core.system.convert.DiasSemanaConverter;

import dgtic.core.system.model.enums.DiasDeSemana;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiasDeSemanaConverter implements AttributeConverter<DiasDeSemana, String> {
//  Estopara convertir de minusculas a muysculas, para la base de datos
    @Override
    public String convertToDatabaseColumn(DiasDeSemana attribute) {
        return attribute == null ? null : attribute.name().toLowerCase(); // guardar en minúsculas
    }

    @Override
    public DiasDeSemana convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DiasDeSemana.valueOf(dbData.toUpperCase());
    }
}
