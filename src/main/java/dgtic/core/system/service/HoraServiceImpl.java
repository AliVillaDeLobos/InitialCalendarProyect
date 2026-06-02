package dgtic.core.system.service;

import dgtic.core.system.exceptions.HoraOcupadException;
import dgtic.core.system.model.entities.DiaSubtarea;
import dgtic.core.system.model.entities.Hora;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.repository.DiaSubtareaRepository;
import dgtic.core.system.repository.HoraRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HoraServiceImpl implements HoraService {
    private HoraRepository horaRepository;
    private DiaSubtareaRepository diaSubtareaRepository;

    public HoraServiceImpl(HoraRepository horaRepository, DiaSubtareaRepository diaSubtareaRepository) {
        this.horaRepository = horaRepository;
        this.diaSubtareaRepository = diaSubtareaRepository;
    }


    @Override
    public Optional<Hora> obtenerHoraPorId(Integer idHora) {
        return horaRepository.findByIdHora(idHora);
    }

    @Override
    public void eliminarHora(Hora hora) {
        horaRepository.deleteById(hora.getIdHora());
    }

    @Override
    public Hora guardar(Hora hora) {
        return horaRepository.save(hora);
    }

    @Override
    public List<Integer> obtenerHoraslibres(Integer idDia) {
        List<Integer> horasOcupadas = horaRepository.findHorasOcupadasPorDia(idDia);
        List<Integer> todasLasHoras = IntStream.rangeClosed(0, 23)
                .boxed()
                .collect(Collectors.toList());
        todasLasHoras.removeAll(horasOcupadas);
        return todasLasHoras;
    }

    @Override
    public List<Hora> obtenerTodasHorasPorUsuario(String email) {
        return horaRepository.findAllByUsuarioEmail(email);
    }

    @Override
    public void eliminarTodas(Collection<Hora> horas) {
        horaRepository.deleteAll(horas);
    }

    @Override
    public Collection<Hora> guardarTodas(Collection<Hora> horas) {
        return horaRepository.saveAll(horas);
    }

    @Transactional
    @Override
    public void insertarHora(Integer idDiaSubtarea, Integer valorHora) {

        DiaSubtarea ds = diaSubtareaRepository.findById(idDiaSubtarea)
                .orElseThrow();

        Hora hora = new Hora();
        hora.setDiaSubtarea(ds);
        hora.setDia(ds.getDia());
        hora.setHora(valorHora);

        try {
            horaRepository.save(hora);
        } catch (DataIntegrityViolationException e) {
            throw new HoraOcupadException("Ya existe una tarea en esa hora.");
        }
    }
}
