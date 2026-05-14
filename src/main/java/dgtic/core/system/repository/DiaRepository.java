package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Dia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaRepository extends JpaRepository<Dia, Integer> {

    List<Dia> findBySemana_Id(Integer idSemana);

}
