package dgtic.core.system.service;

import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.mapper.SubtareaMapper;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.repository.SubtareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubtareaServiceImpl implements SubtareaService {
    private final SubtareaRepository subtareaRepository;
    private final SubtareaMapper mapper;

    @Autowired
    public SubtareaServiceImpl(SubtareaRepository subtareaRepository, SubtareaMapper mapper) {
        this.subtareaRepository = subtareaRepository;
        this.mapper = mapper;
    }



    @Override
    public Subtarea actualizarEstado(Integer idSubtarea) {
        Subtarea subtarea = subtareaRepository.findById(idSubtarea).orElseThrow(
                () -> new ResourceNotFoundException("Subtarea no encontrada"));
        subtarea.setEstado(!subtarea.getEstado());
        return subtareaRepository.save(subtarea);
    }

    @Override
    public String eliminar(Integer id, String mensaje) {
        Subtarea subtarea = subtareaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada"));
        subtarea.setEliminada(true);
        subtareaRepository.save(subtarea);
        subtareaRepository.eliminarSubtareaConMensaje(id, mensaje);
        String mensajeRespuesta = subtareaRepository.obtenerMensajeProcedimiento();
        return mensajeRespuesta;
    }

    @Override
    public Subtarea guardar(Subtarea subtarea) {
        subtarea.setFechaCreacion(LocalDate.now());
        return subtareaRepository.save(subtarea);
    }

//        ESte para no actualizar la fecha a la de modificacion
    @Override
    public Subtarea modificar(Subtarea subtarea) {
        return subtareaRepository.save(subtarea);
    }

    @Override
    public Optional<Subtarea> buscarPorId(Integer id) {
        return subtareaRepository.findById(id);
    }

    @Override
    public Page<SubtareaDto> buscarPorNombreYTarea(String nombre, Integer idTarea, Pageable pageable) {
        List<Subtarea> subtareasEntity = subtareaRepository.findByTarea_idTareaAndNombreContainingIgnoreCase(idTarea, nombre).stream().toList();
        List<SubtareaDto> subtareasDto = subtareasEntity.stream().map(mapper::toDto).collect(Collectors.toList());
        int inicio = Math.min((int) pageable.getOffset(), subtareasDto.size());
        int fin = Math.min( inicio + pageable.getPageSize(), subtareasDto.size());
        Page<SubtareaDto> page = new PageImpl<>(subtareasDto.subList(inicio, fin), pageable, subtareasDto.size());
        return page;
    }

    @Override
    public Page<SubtareaDto> tareasActivasDeTarea(Integer idTarea, Pageable pageable) {
        List<Subtarea> subtareasEntity = subtareaRepository.findByTarea_IdTareaAndEstado(idTarea, true).stream().collect(Collectors.toList());
        List<SubtareaDto> subtareasDto = subtareasEntity.stream().map(mapper::toDto).collect(Collectors.toList());
        int inicio = Math.min((int) pageable.getOffset(), subtareasDto.size());
        int fin = Math.min( inicio + pageable.getPageSize(), subtareasDto.size());
        Page<SubtareaDto> page = new PageImpl<>(subtareasDto.subList(inicio, fin), pageable, subtareasDto.size());
        return page;
    }


    @Override
    public Page<SubtareaDto> tareasInactivasDeTarea(Integer idTarea, Pageable pageable) {
        List<Subtarea> subtareasEntity = subtareaRepository.findByTarea_IdTareaAndEstado(idTarea, false).stream().collect(Collectors.toList());
        List<SubtareaDto> subtareasDto = subtareasEntity.stream().map(mapper::toDto).collect(Collectors.toList());
        int inicio = Math.min((int) pageable.getOffset(), subtareasDto.size());
        int fin = Math.min( inicio + pageable.getPageSize(), subtareasDto.size());
        Page<SubtareaDto> page = new PageImpl<>(subtareasDto.subList(inicio, fin), pageable, subtareasDto.size());
        return page;
    }

    @Override
    public Page<SubtareaDto> todasSubtareaUsuario(String email, Pageable pageable) {
        List<SubtareaDto> subtareasDto = subtareaRepository.findDtoByUsuarioId(email).stream().toList();
        int inicio = Math.min((int) pageable.getOffset(), subtareasDto.size());
        int fin = Math.min( inicio + pageable.getPageSize(), subtareasDto.size());
        Page<SubtareaDto> page = new PageImpl<>(subtareasDto.subList(inicio, fin), pageable, subtareasDto.size());
        return page;
    }

    @Override
    public Page<SubtareaDto> todasPorUsuarioYNombre(String email, String nombre, Pageable pageable) {
        return subtareaRepository.findDtoByUsuarioYNombre(email, nombre, pageable);
    }

    @Override
    public String obtenerDescripcion(Integer idSubtarea) {
        Subtarea subtarea = subtareaRepository.findById(idSubtarea).orElseThrow(
                () -> new ResourceNotFoundException("Subtarea no encontrada"));
        if (subtarea.getDescripcion() != null && subtarea.getDescripcion().getTexto() != null) {
            return subtarea.getDescripcion().getTexto();
        }
        return "No hay descripción";
    }

    @Override
    public List<Subtarea> obtenerConDiasYHoras(String email) {
        return subtareaRepository.findConDiasYHoras(email);
    }

    @Override
    public List<Subtarea> obtenerTodasPorUsuario(String email) {
        return subtareaRepository.findAllByUsuarioEmail(email);
    }

    @Override
    public List<Subtarea> obtenerPorIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return subtareaRepository.findAllById(ids);
    }
}
