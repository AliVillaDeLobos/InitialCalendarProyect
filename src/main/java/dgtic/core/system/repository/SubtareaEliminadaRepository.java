package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.SubtareaEliminada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SubtareaEliminadaRepository extends JpaRepository<SubtareaEliminada, Integer> {
    Collection<SubtareaEliminada> findSubtareaEliminadaBySubtarea_Id (Integer idSubtarea);

    @Query("""
        SELECT se
        FROM SubtareaEliminada se
        JOIN se.subtarea s
        JOIN s.tarea t
        JOIN t.claseTarea ct
        JOIN ct.usuario u
        WHERE u.email = :email
        ORDER BY se.fechaEliminacion DESC
    """)
    List<SubtareaEliminada> findAllByUsuarioEmail(@Param("email") String email);

    void deleteBySubtarea_Id (Integer idSubtarea);

}
