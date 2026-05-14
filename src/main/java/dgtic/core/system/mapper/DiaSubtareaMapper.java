package dgtic.core.system.mapper;

import dgtic.core.system.dto.DiasSubtareaDto;
import dgtic.core.system.model.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DiaSubtareaMapper {
    private final ModelMapper modelMapper;

    public DiaSubtareaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<DiasSubtareaDto> toDto(Subtarea subtarea){
        return subtarea.getDiaSubtareas().stream().map(ds -> {
            DiasSubtareaDto dto = new DiasSubtareaDto();
            dto.setIdDiaSubtarea(ds.getId());
            dto.setIdSubtarea(subtarea.getId());
            dto.setNombreSubtarea(subtarea.getNombre());

            if(subtarea.getTarea() != null && subtarea.getTarea().getClaseTarea() != null) {
                dto.setColorClase(subtarea.getTarea().getClaseTarea().getColor().getCodigo());
            }

            if(ds.getDia() != null) {
                dto.setIdSemana(ds.getDia().getSemana().getId());
                dto.setFechaInicioSemana(ds.getDia().getSemana().getFechaInicio());
                dto.setFechaFinSemana(ds.getDia().getSemana().getFechaFin());
                dto.setNumeroSemana(ds.getDia().getSemana().getNumeroSemana());
                dto.setNombreDia(ds.getDia().getNombreDia().name());
            }
            dto.setHoras(ds.getHoras().stream()
                    .map(Hora::getHora)
                    .sorted()
                    .collect(Collectors.toList()));

            dto.setEstado(ds.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    //Aplana la lista para devolver todo unificado
    public List<DiasSubtareaDto> toDtoList (Collection<Subtarea> subtareas){
        return  subtareas.stream()
                .flatMap(s -> toDto(s).stream())
                .collect(Collectors.toList());
        }

    public Set<DiaSubtarea> dtosToDiaSubtareas(Subtarea subtarea, List<DiasSubtareaDto> dtos, Map<Integer, Semana> semanasMap) {
        Set<DiaSubtarea> diaSubtareas = new HashSet<>();

        for (DiasSubtareaDto dto : dtos) {
            // Obtener la semana usando el idSemana
            Semana semana = semanasMap.get(dto.getIdSemana());
            if (semana == null) {
                throw new IllegalArgumentException("No se encontró la Semana para id: " + dto.getIdSemana());
            }

            // Buscar el Dia dentro de la semana usando el nombre del día
            Dia dia = semana.getDias().stream()
                    .filter(d -> d.getNombreDia().name().equalsIgnoreCase(dto.getNombreDia()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No se encontró el Dia '" + dto.getNombreDia() + "' en la Semana id: " + dto.getIdSemana()
                    ));

            DiaSubtarea ds = new DiaSubtarea();
            ds.setSubtarea(subtarea);
            ds.setDia(dia);
            ds.setEstado(dto.getEstado());

            // Mapear las horas
            Set<Hora> horas = new HashSet<>();
            if (dto.getHoras() != null && !dto.getHoras().isEmpty()) {
                for (Integer h : dto.getHoras()) {
                    Hora hora = new Hora();
                    hora.setHora(h);
                    hora.setDiaSubtarea(ds);
                    horas.add(hora);
                }
            }
            ds.setHoras(horas);

            diaSubtareas.add(ds);
        }

        subtarea.setDiaSubtareas(diaSubtareas);
        return diaSubtareas;
    }


}
