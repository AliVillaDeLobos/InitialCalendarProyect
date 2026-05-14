package dgtic.core.system.repository;

import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.model.entities.Hora;
import dgtic.core.system.model.entities.Subtarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SubtareaRepository extends JpaRepository<Subtarea, Integer> {

    Optional<Subtarea> findById(Integer idSubtarea);
    Optional<Subtarea> findByTarea_idTareaAndNombreContainingIgnoreCase(Integer idTarea, String nombre);
    Collection<Subtarea> findByTarea_ClaseTarea_Usuario_EmailAndEliminada(String email, Boolean eliminada);
    Collection<Subtarea> findByTarea_IdTareaAndEstado(Integer idTarea, Boolean estado);


//    List<Subtarea> findByTarea_ClaseTarea_Usuario_EmailAndDiaSubtareasNotEmpty(String email);

    @Query("""
    SELECT new dgtic.core.system.dto.SubtareaDto(
        s.id,
        s.nombre,
        s.fechaCreacion,
        t.nombre,
        s.eliminada,
        s.estado
    )
    FROM Subtarea s
    JOIN s.tarea t
    JOIN t.claseTarea ct
    JOIN ct.usuario u
    WHERE u.email = :email AND s.eliminada = false
""")
    List<SubtareaDto> findDtoByUsuarioId(@Param("email") String email);

    @Query("""
    SELECT new dgtic.core.system.dto.SubtareaDto(
        s.id, s.nombre, s.fechaCreacion, t.nombre, s.eliminada, s.estado
    )
    FROM Subtarea s
    JOIN s.tarea t
    JOIN t.claseTarea ct
    JOIN ct.usuario u
    WHERE u.email = :email AND s.nombre LIKE CONCAT( '%', :nombre, '%')
""")
    Page<SubtareaDto> findDtoByUsuarioYNombre(@Param("email") String email,
                                              @Param("nombre") String nombre,
                                              Pageable pageable);


    @Query("""
    SELECT DISTINCT s
    FROM Subtarea s
    JOIN s.tarea t
    JOIN t.claseTarea ct
    JOIN ct.usuario u
    LEFT JOIN FETCH s.diaSubtareas ds
    LEFT JOIN FETCH ds.horas h
    WHERE u.email = :email
""")
    List<Subtarea> findConDiasYHoras(@Param("email") String email);

    @Query("""
           SELECT st
           FROM Subtarea st
           JOIN st.tarea t
           JOIN t.claseTarea ct
           JOIN ct.usuario u
           WHERE u.email = :email
           """)
    List<Subtarea> findAllByUsuarioEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "CALL eliminar_subtarea(:idSubtarea, :mensajeEliminacion, @mensaje)", nativeQuery = true)
    void eliminarSubtareaConMensaje(@Param("idSubtarea") Integer idSubtarea,
                                    @Param("mensajeEliminacion") String mensajeEliminacion);

    @Query(value = "SELECT @mensaje", nativeQuery = true)
    String obtenerMensajeProcedimiento();

}
