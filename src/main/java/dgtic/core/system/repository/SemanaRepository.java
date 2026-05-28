package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Semana;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface SemanaRepository extends JpaRepository<Semana, Integer> {

    Page<Semana> findByAnioSemanaBetween(Integer inicio, Integer fin, Pageable pageable);
    Optional<Semana> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT s FROM Semana s WHERE :fecha BETWEEN s.fechaInicio AND s.fechaFin")
    Optional<Semana> findSemanaActual(@Param("fecha") LocalDate fecha);

    Boolean existsByNumeroSemanaAndAnioSemana(Integer numeroSemana, Integer anioSemana);

}
