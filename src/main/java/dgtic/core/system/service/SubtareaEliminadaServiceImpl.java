package dgtic.core.system.service;

import dgtic.core.system.dto.SubtareasEliminadasDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.mapper.SubtareaEliminadaMapper;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.SubtareaEliminada;
import dgtic.core.system.repository.SubtareaEliminadaRepository;
import dgtic.core.system.repository.SubtareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubtareaEliminadaServiceImpl implements SubtareaEliminadaService {
    private final SubtareaEliminadaRepository subtareaEliminadaRepository;
    private final SubtareaEliminadaMapper mapper;
    private final SubtareaService subtareaService;
    private final SubtareaRepository subtareaRepository;

    @Autowired
    public SubtareaEliminadaServiceImpl(SubtareaEliminadaRepository subtareaEliminadaRepository,
                                        SubtareaEliminadaMapper mapper, SubtareaService subtareaService,
                                        SubtareaRepository subtareaRepository) {
        this.subtareaEliminadaRepository = subtareaEliminadaRepository;
        this.mapper = mapper;
        this.subtareaService = subtareaService;
        this.subtareaRepository = subtareaRepository;
    }

    @Override
    public Page<SubtareasEliminadasDto> subtareasEliminadas(Pageable pageable, List<Subtarea> subtareas, String email) {
        List<SubtareaEliminada> eliminadas = subtareaEliminadaRepository.findAllByUsuarioEmail(email);
        List<SubtareasEliminadasDto> dtos = mapper.toDto(eliminadas, subtareas);

        int inicio = Math.min((int) pageable.getOffset(), dtos.size());
        int fin = Math.min( inicio + pageable.getPageSize(), dtos.size());
        Page<SubtareasEliminadasDto> page = new PageImpl<>(dtos.subList(inicio, fin), pageable, dtos.size());
        return page;
    }

    @Override
    public Collection<SubtareaEliminada> todasPorUsuario(String email) {
        return subtareaEliminadaRepository.findAllByUsuarioEmail(email);

    }

    @Transactional
    public void restaurarSubtarea(Integer idSubtareaEliminada) {
        SubtareaEliminada subtareaE = subtareaEliminadaRepository.findById(idSubtareaEliminada).orElseThrow(
                () -> new ResourceNotFoundException("Subtarea Eliminada no encontrada"));
        Subtarea sub = subtareaService.buscarPorId(subtareaE.getSubtarea().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Subtarea no encontrada"));
        sub.setEliminada(false);
        subtareaService.guardar(sub);
        subtareaEliminadaRepository.deleteById(idSubtareaEliminada);
    }

    @Transactional
    @Override
    public void eliminarSubtarea(Integer idSubtarea, String mensaje) {

        Subtarea subtarea = subtareaRepository.findById(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("NO existe una subtarea con el ID " + idSubtarea));

        subtarea.setEliminada(true);

        SubtareaEliminada eliminada = new SubtareaEliminada();
        eliminada.setSubtarea(subtarea);
        eliminada.setMensaje(mensaje);
        eliminada.setFechaEliminacion(LocalDate.now());

        subtareaEliminadaRepository.save(eliminada);
    }
}
