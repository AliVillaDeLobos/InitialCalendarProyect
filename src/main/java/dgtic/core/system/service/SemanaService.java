package dgtic.core.system.service;

import dgtic.core.system.model.entities.Semana;

import java.time.LocalDate;
import java.util.Optional;

public interface SemanaService {
    Optional<Semana> bucarSemanaPorFecha(LocalDate fecha);
    Semana guardar(Semana semana);
    void eliminar(Integer id);
    Optional<Semana> buscarSemanaPorId(Integer id);
    Semana obtenerSemanaActual();

}
