package dgtic.core.system.mapper;

import dgtic.core.system.dto.ClaseTareaDto;
import dgtic.core.system.dto.TareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.service.ClaseTareaService;
import dgtic.core.system.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClaseTareaMapper {
    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;
    private ClaseTareaService claseTareaService;

    public ClaseTareaMapper(ModelMapper modelMapper, UsuarioService usuarioService, ClaseTareaService claseTareaService) {
        this.modelMapper = modelMapper;
        this.usuarioService = usuarioService;
        this.claseTareaService = claseTareaService;
    }

    //Entity a DTO
    public ClaseTareaDto toDto(ClaseTarea claseTarea) {
        ClaseTareaDto dto = modelMapper.map(claseTarea, ClaseTareaDto.class);

        if (dto.getIdUsuario() != null) {
            Usuario usr = usuarioService.findById(dto.getIdUsuario()).orElseThrow(
                    () -> new ResourceNotFoundException("El usuario con el idSubtarea " + dto.getIdUsuario() + "no encontrado"));
            dto.setIdUsuario(usr.getIdUsuario());
        }
        if (claseTarea.getColor() != null) {

        }
        if(claseTarea.getTareas() != null) {
            List<TareaDto>  tareas = claseTarea.getTareas().stream()
                    .map(tarea -> modelMapper.map(tarea, TareaDto.class)).collect(Collectors.toList());
            dto.setTareas(tareas);
        }
        return dto;
    }

    //DTOs List
    public List<ClaseTareaDto> toDtoList(List<ClaseTarea> claseTareas) {
        return claseTareas.stream().map(this::toDto).collect(Collectors.toList());
    }

    //DTO a Entity
    public ClaseTarea toEntity(ClaseTareaDto dto) {
        ClaseTarea entity = modelMapper.map(dto, ClaseTarea.class);
        if (dto.getIdUsuario() != null) {
            Usuario usr = usuarioService.findById(dto.getIdUsuario()).orElseThrow(
                    () -> new ResourceNotFoundException("El usuario con el idSubtarea que mandaste no existe"));
            entity.setUsuario(usr);
        }

        if (dto.getTareas() != null) {
            List<Tarea> tareas = dto.getTareas().stream()
                    .map(t -> modelMapper.map(t, Tarea.class)).collect(Collectors.toList());
            entity.setTareas(tareas);
        }
        return entity;
    }
}
