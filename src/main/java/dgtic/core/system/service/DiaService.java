package dgtic.core.system.service;

import dgtic.core.system.model.entities.Dia;

import java.util.List;
import java.util.Optional;

public interface DiaService {
    Dia guardar(Dia dia);
    Optional<Dia> obtenerPorId(Integer idDia);
    void eliminar(Integer idDia);

    List<Dia> obtenerDiasPorSemana(Integer idSemana);
}
