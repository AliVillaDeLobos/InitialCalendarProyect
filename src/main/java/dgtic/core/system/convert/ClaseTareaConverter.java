package dgtic.core.system.convert;

import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.service.ClaseTareaService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClaseTareaConverter implements Converter<String, ClaseTarea> {

    private final ClaseTareaService claseTareaService;

    public ClaseTareaConverter(ClaseTareaService claseTareaService) {
        this.claseTareaService = claseTareaService;
    }

    @Override
    public ClaseTarea convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        Integer idClaseTarea = Integer.valueOf(id);
        return claseTareaService.findById(idClaseTarea).orElse(null);
    }
}
