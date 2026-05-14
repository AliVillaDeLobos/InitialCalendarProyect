package dgtic.core.system.service;

import dgtic.core.system.model.entities.Hora;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.repository.HoraRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HoraServiceImpl implements HoraService {
    private HoraRepository horaRepository;

    public HoraServiceImpl(HoraRepository horaRepository) {
        this.horaRepository = horaRepository;
    }

    @Override
    public void agregarHora(Integer idDiaSubtarea, Integer hora) {
        try {
            horaRepository.insertarHora(idDiaSubtarea, hora);
        } catch (Exception e) {
                //Puedo crear una EXCEPTION
            throw new RuntimeException("Error al insertar hora: " + e.getMessage());
        }
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
}
