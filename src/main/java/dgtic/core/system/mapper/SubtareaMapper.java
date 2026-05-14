package dgtic.core.system.mapper;


import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.model.entities.Subtarea;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubtareaMapper {
    // Tener mucho cuidado como pones el formato de la fecha, si lo cambias a '/' altera el parseo
    private static final DateTimeFormatter dateF = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final ModelMapper modelMapper;


    public SubtareaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

    }

    //Entity a DTO
    public SubtareaDto toDto(Subtarea subtarea) {
        SubtareaDto dto = modelMapper.map(subtarea, SubtareaDto.class);
        if (subtarea.getTarea() != null &&
                subtarea.getTarea().getClaseTarea() != null &&
                subtarea.getTarea().getClaseTarea().getColor() != null) {
            dto.setCodigoColor(subtarea.getTarea().getClaseTarea().getColor().getCodigo());
        }
        return dto;
    }

    // Lista DTOs
    public List<SubtareaDto> toDtoList(List<Subtarea> subtareas) {
        return subtareas.stream().map(this::toDto).collect(Collectors.toList());
    }

    //DTO a Entity
    public Subtarea toEntity(SubtareaDto dto) {
        Subtarea subtarea = modelMapper.map(dto, Subtarea.class);
        return subtarea;
    }
}
