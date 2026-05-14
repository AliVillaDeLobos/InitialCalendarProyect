package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.enums.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
//      Consultas Derivadas
    List<Tarea> findTareasByClaseTarea_Usuario_Email(String usuarioEmail);
    List<Tarea> findTareaByClaseTarea_ColorAndClaseTarea_Usuario_Email(Color color, String usuarioEmail);

    Optional<Tarea> findByNombre(String nombre);
    Optional<Tarea> findByIdTareaAndClaseTarea_Usuario_Email(Integer idTarea, String usuarioEmail);

    //    Consultas Nativas
    // Aquí permito que se pase null como valor es solo para buscar por el nombre o color de lo contrario devuelve
    // toda la lista
    @Query(value = """
            SELECT t FROM Tarea t
            WHERE (:color IS NULL OR t.claseTarea.color = :color)
                        AND
                    (:nombre IS NULL OR t.claseTarea.nombre = :nombre)""")
    List<Tarea> buscarPorColorONombreClaseTarea(@Param("color") Color color, @Param("nombre") String nombre);


}
