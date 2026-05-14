package dgtic.core.system.service;

import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.enums.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TareaService {

    Tarea save(Tarea tarea);
    Optional<Tarea> findById(Integer id);
    Optional<Tarea> findByIdAndUsuario(Integer id, String email);
    Optional<Tarea> findByNombre(String nombre);
    void deleteById(Integer id);
    Page<Tarea> findTareasUsuario(String email, Pageable pageable);
    Collection<Tarea> findTareasUsuarioCollection(String email);
    Page<Tarea> buscarPorColorONombreCT(Color color, String nombre, Pageable pageable);
    Page<Tarea> buscarPorColorYEmailP(Color color, String email, Pageable pageable);
    List<Tarea> buscarPorColorYEmail(Color color, String email);

}
