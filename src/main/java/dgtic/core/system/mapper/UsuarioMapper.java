package dgtic.core.system.mapper;

import dgtic.core.system.dto.ClaseTareaDto;
import dgtic.core.system.dto.UsuarioDto;
import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.entities.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

    }


    //Entity a DTO
    public UsuarioDto toDto(Usuario usuario) {
        UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
//        dto.setPassword(usuario.getUsuarioPassword()); //Par no exponer la salida de la password

        if(usuario.getClaseTareas() != null){
            List<ClaseTareaDto> claseTareaDtosList = usuario.getClaseTareas().stream()
                    .map(t -> modelMapper.map(t,  ClaseTareaDto.class)).toList();
            dto.setClaseTareas(claseTareaDtosList);
        }
        return dto;
    }

    //Lista de DTOs
    public List<UsuarioDto> toDtoList(List<Usuario> usuarios) {
        return usuarios.stream().map(this::toDto).collect(Collectors.toList());
    }

    //DTO a Entity
    public Usuario toEntity(UsuarioDto dto) {
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setUsuarioPassword(dto.getPassword());
        if (dto.getClaseTareas() != null && !dto.getClaseTareas().isEmpty()) {
            List<ClaseTarea> claseTareas = dto.getClaseTareas().stream()
                    .map(t -> modelMapper.map(t, ClaseTarea.class)).toList();
            usuario.setClaseTareas(claseTareas);
        }
        return usuario;
    }

}
