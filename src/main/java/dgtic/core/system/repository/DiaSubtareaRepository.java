package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Dia;
import dgtic.core.system.model.entities.DiaSubtarea;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.enums.DiasDeSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DiaSubtareaRepository extends JpaRepository<DiaSubtarea, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM DiaSubtarea ds WHERE ds.subtarea = :subtarea AND ds.dia.nombreDia = :nombreDia")
    void deleteBySubtareaAndDia(@Param("subtarea") Subtarea subtarea,
                                @Param("nombreDia") DiasDeSemana nombreDia);

    Optional<DiaSubtarea> findBySubtareaAndDia(Subtarea substarea, Dia dia);

    @Query("""
SELECT ds
FROM DiaSubtarea ds
LEFT JOIN FETCH ds.dia d
LEFT JOIN FETCH d.semana sem
LEFT JOIN FETCH ds.horas h
WHERE ds.subtarea.id = :idSubtarea
""")
    List<DiaSubtarea> findBySubtareaId(@Param("idSubtarea") Integer idSubtarea);

    @Query("""
    SELECT ds.dia.id
    FROM DiaSubtarea ds
    WHERE ds.subtarea.id = :idSubtarea
    AND ds.dia.id IN :diaIds
    """)
    Set<Integer> findExistingDiaIdsBySubtareaAndDiaIds(Integer idSubtarea, List<Integer> diaIds);
}
