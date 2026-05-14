package dgtic.core.system.service;

import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.enums.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClaseTareaService {
    List<ClaseTarea> findAllByColor_Nombre(String nombreColor, String email);
    Optional<ClaseTarea>findById(Integer id);
    ClaseTarea save(ClaseTarea tarea);
    void delete(Integer idClaseTarea);
    List<ClaseTarea> findAllByUsuarioEmail(String email);
    Collection<Color> obtenerColoresUsados(String email);
    Collection<Color> obtenerColoresDisponibles(String email);
    Page<ClaseTarea> findClaseTareasUsuario(String email, Pageable pageable);
    Page<ClaseTarea> findClaseTareasColorYUsuario(String nombreColor, String email, Pageable pageable);
    Page<ClaseTarea> findClaseTareasNombreYUsuario(String nombre, String email, Pageable pageable);


}
