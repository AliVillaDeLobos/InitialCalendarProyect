package dgtic.core.system.service;

import dgtic.core.system.dto.DiasSubtareaDto;
import dgtic.core.system.model.entities.Dia;
import dgtic.core.system.model.entities.DiaSubtarea;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.enums.DiasDeSemana;

import java.util.List;
import java.util.Map;

public interface DiasSubtareaService {
    void asignarDiasYHoras (Subtarea subtarea, Map<DiasDeSemana, List<Integer>> diasConHoras);
    void eliminarDiasYHorasPorSubtarea(Subtarea subtarea, Dia dia);
    void eliminarDiaDeSubtarea(Subtarea subtarea, DiasDeSemana dia);
    void agregarHorasADiaSubtarea(Integer idSubtarea, Integer idDia, List<Integer> nuevasHoras);
    DiaSubtarea guardar(DiaSubtarea diaSubtarea);
//    List<DiasSubtareaDto> listarDiasYHorasPorSubtarea(List<Subtarea> subtareas);
    List<DiasSubtareaDto> listarDiasYHorasPorSubtarea(String email);
    public void agregarDiasSubtarea(Integer idSubtarea, List<DiasDeSemana> diasSemana, Integer idSemana);


}
