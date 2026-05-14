package dgtic.core.system.mapper;

import dgtic.core.system.dto.ClaseTareaDto;
import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.dto.TareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.service.ClaseTareaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TareaMapper {
    private final ModelMapper modelMapper;
    private final ClaseTareaService claseTareaService;

    public TareaMapper(ModelMapper modelMapper, ClaseTareaService claseTareaService) {
        this.modelMapper = modelMapper;
        this.claseTareaService = claseTareaService;
    }

    //Entity a DTO
    public TareaDto toDto(Tarea tarea) {
        TareaDto dto = modelMapper.map(tarea, TareaDto.class);
        if (tarea.getClaseTarea() != null) {
            ClaseTareaDto claseDto = modelMapper.map(tarea.getClaseTarea(), ClaseTareaDto.class);
            dto.setClaseTarea(claseDto);
        }
        if (tarea.getSubtareas() != null && !tarea.getSubtareas().isEmpty()) {
            List<SubtareaDto> subtareas = tarea.getSubtareas().stream()
                    .map(s -> modelMapper.map(s, SubtareaDto.class)).toList();
            dto.setSubtareas(subtareas);
        }
        return dto;
    }

    //Lista DTOs
    public List<TareaDto> toDtoList(List<Tarea> tareas) {
        return tareas.stream().map(this::toDto).collect(Collectors.toList());
    }

    //DTO a Entity
    public Tarea toEntity(TareaDto dto) {
        Tarea tarea = modelMapper.map(dto, Tarea.class);
        if (dto.getClaseTarea() != null) {
            ClaseTarea ct = claseTareaService.findById(dto.getClaseTarea().getIdClaseTarea()).orElseThrow(
                    () -> new ResourceNotFoundException("Clase Tarea no encontrada"));
            tarea.setClaseTarea(ct);
        }
        if (dto.getSubtareas() != null && !dto.getSubtareas().isEmpty()) {
            List<Subtarea> subtareas = dto.getSubtareas().stream().map(
                    s -> modelMapper.map(s, Subtarea.class)).toList();
            tarea.setSubtareas(subtareas);
        }
        return tarea;
    }
}
