package dgtic.core.system.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    //Es necesario para utilizar el model mapper en la app
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
