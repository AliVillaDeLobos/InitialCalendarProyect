package dgtic.core.system.mapper;

import dgtic.core.system.dto.SubtareasEliminadasDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.SubtareaEliminada;
import dgtic.core.system.repository.SubtareaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SubtareaEliminadaMapper {


    public List<SubtareasEliminadasDto> toDto(List<SubtareaEliminada> eliminados, List<Subtarea> subtareas) {
        Map<Integer, Subtarea> subtareaMap = subtareas.stream()
                .collect(Collectors.toMap(Subtarea::getId, s -> s));

        List<SubtareasEliminadasDto> dtos = new ArrayList<>();
        for (SubtareaEliminada se : eliminados) {
            Subtarea s = subtareaMap.get(se.getSubtarea().getId());
            if (s != null) {
                SubtareasEliminadasDto dto = SubtareasEliminadasDto.builder()
                        .idSubtareaEliminada(se.getId())
                        .estado(s.getEstado())
                        .fechaEliminacion(se.getFechaEliminacion())
                        .nombre(s.getNombre())
                        .nombreTarea(s.getTarea().getNombre())
                        .mensaje(se.getMensaje())
                        .build();
                dtos.add(dto);
            }
        }
        return dtos;
    }



}
