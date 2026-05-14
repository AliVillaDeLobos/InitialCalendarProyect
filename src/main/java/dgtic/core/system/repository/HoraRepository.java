package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Hora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoraRepository extends JpaRepository<Hora, Integer> {

    // Para insertar un DiaTarea con Hora
    @Procedure(name = "Hora.insertarHora")
    void insertarHora(
            @Param("p_id_dia_subtarea") Integer idDiaSubtarea,
            @Param("p_hora") Integer hora
    );
    Optional<Hora> findByIdHora(Integer idHora);

    @Query("SELECT h.hora FROM Hora h WHERE h.dia.id = :idDia")
    List<Integer> findHorasOcupadasPorDia(@Param("idDia") Integer idDia);

    @Query("""
           SELECT h
           FROM Hora h
           JOIN h.diaSubtarea ds
           JOIN ds.subtarea st
           JOIN st.tarea t
           JOIN t.claseTarea ct
           JOIN ct.usuario u
           WHERE u.email = :email
           """)
    List<Hora> findAllByUsuarioEmail(@Param("email") String email);

}
