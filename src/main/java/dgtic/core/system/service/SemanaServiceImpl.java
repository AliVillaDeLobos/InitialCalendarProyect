package dgtic.core.system.service;

import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Semana;
import dgtic.core.system.repository.SemanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SemanaServiceImpl implements SemanaService {
    private SemanaRepository semanaRepository;

    @Autowired
    public SemanaServiceImpl(SemanaRepository semanaRepository) {
        this.semanaRepository = semanaRepository;
    }


    @Override
    public Optional<Semana> bucarSemanaPorFecha(LocalDate fecha) {
        return semanaRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(fecha, fecha);
    }

    @Override
    public Optional<Semana> buscarSemanaPorId(Integer id) {
        return semanaRepository.findById(id);
    }

    @Override
    public Semana guardar(Semana semana) {
        return semanaRepository.save(semana);
    }

    @Override
    public void eliminar(Integer id) {
        semanaRepository.deleteById(id);
    }

    @Override
    public Semana obtenerSemanaActual() {
        LocalDate hoy = LocalDate.now();
        return semanaRepository.findSemanaActual(hoy).orElseThrow(
                () -> new ResourceNotFoundException("Hubo un error al buscar la semana por fecha"));

    }
}
