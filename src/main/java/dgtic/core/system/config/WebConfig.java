package dgtic.core.system.config;

import dgtic.core.system.convert.ClaseTareaConverter;
import dgtic.core.system.convert.ColorMvcConverter;
import dgtic.core.system.convert.DiasSemanaConverter.EstadoTareaMvcConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ColorMvcConverter colorMvcConverter;
    private final ClaseTareaConverter claseTareaConverter;
    private final EstadoTareaMvcConverter estadoTareaMvcConverter;

    public WebConfig(ClaseTareaConverter claseTareaConverter, ColorMvcConverter colorMvcconverter,
                     EstadoTareaMvcConverter estadoTareaMvcConverter) {
        this.claseTareaConverter = claseTareaConverter;
        this.colorMvcConverter = colorMvcconverter;
        this.estadoTareaMvcConverter = estadoTareaMvcConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(claseTareaConverter);
        registry.addConverter(new ColorMvcConverter());
        registry.addConverter(estadoTareaMvcConverter);
    }


}
