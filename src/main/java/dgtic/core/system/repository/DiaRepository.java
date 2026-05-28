package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Dia;
import dgtic.core.system.model.enums.DiasDeSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaRepository extends JpaRepository<Dia, Integer> {

    List<Dia> findBySemana_Id(Integer idSemana);
    List<Dia> findBySemana_IdAndNombreDiaIn(Integer idSemana, List<DiasDeSemana> diasSemans);
    Boolean existsByFecha(LocalDate  fecha);
}
