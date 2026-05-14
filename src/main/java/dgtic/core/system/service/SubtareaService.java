package dgtic.core.system.service;

import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.model.entities.Subtarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubtareaService {
    String eliminar(Integer id, String mensaje);
    Subtarea guardar(Subtarea subtarea);
    Subtarea modificar(Subtarea subtarea);
    Subtarea actualizarEstado(Integer idSubtarea);
    Optional<Subtarea> buscarPorId(Integer id);
    Page<SubtareaDto> todasSubtareaUsuario(String email, Pageable pageable);
    Page<SubtareaDto> todasPorUsuarioYNombre(String email, String nombre, Pageable pageable);
    List<Subtarea> obtenerConDiasYHoras(String email);
    List<Subtarea> obtenerTodasPorUsuario(String email);
    String obtenerDescripcion(Integer idSubtarea);
    List<Subtarea> obtenerPorIds(List<Integer> idSubtarea);


    Page<SubtareaDto> buscarPorNombreYTarea(String nombre, Integer idTarea, Pageable pageable);
    Page<SubtareaDto> tareasActivasDeTarea(Integer idTarea, Pageable pageable);
    Page<SubtareaDto> tareasInactivasDeTarea(Integer idTarea, Pageable pageable);
}
