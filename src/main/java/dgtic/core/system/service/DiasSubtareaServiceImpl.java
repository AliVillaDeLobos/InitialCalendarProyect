package dgtic.core.system.service;

import dgtic.core.system.dto.DiasSubtareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.mapper.DiaSubtareaMapper;
import dgtic.core.system.model.entities.*;
import dgtic.core.system.model.enums.DiasDeSemana;
import dgtic.core.system.model.enums.EstadoTarea;
import dgtic.core.system.repository.DiaRepository;
import dgtic.core.system.repository.DiaSubtareaRepository;
import dgtic.core.system.repository.SubtareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiasSubtareaServiceImpl implements DiasSubtareaService {
    private final DiaSubtareaRepository diaSubtareaRepository;
    private final HoraService horaService;
    private final DiaService diaService;
    private final SubtareaService subtareaService;
    private final SemanaService semanaService;
    private final DiaSubtareaMapper mapper;
    private final SubtareaRepository subtareaRepository;
    private final DiaRepository diaRepository;



    @Autowired
    public DiasSubtareaServiceImpl(DiaSubtareaRepository diaSubtareaRepository, HoraService horaService,
                                   SemanaService semanaService, DiaService diaService, SubtareaService subtareaService,
                                   DiaSubtareaMapper diaSubtareaMapper, SubtareaRepository subtareaRepository,
                                   DiaRepository diaRepository) {
        this.diaSubtareaRepository = diaSubtareaRepository;
        this.horaService = horaService;
        this.semanaService = semanaService;
        this.diaService = diaService;
        this.subtareaService = subtareaService;
        this.mapper = diaSubtareaMapper;
        this.subtareaRepository = subtareaRepository;
        this.diaRepository = diaRepository;
    }

    @Override
    public void asignarDiasYHoras(Subtarea subtarea, Map<DiasDeSemana, List<Integer>> diasConHoras) {

        Semana semana = semanaService.bucarSemanaPorFecha(subtarea.getFechaCreacion())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró semana para la fecha: " + subtarea.getFechaCreacion()));

        for (Dia dia : semana.getDias()) {
            List<Integer> horasSeleccionadas = diasConHoras.get(dia.getNombreDia());
            if (horasSeleccionadas != null && !horasSeleccionadas.isEmpty()) {

                DiaSubtarea ds = new DiaSubtarea();
                ds.setSubtarea(subtarea);
                ds.setDia(dia);
                ds.setEstado(EstadoTarea.PENDIENTE);

                diaSubtareaRepository.save(ds);

                for (Integer h : horasSeleccionadas) {
                    Hora hora = new Hora();
                    hora.setHora(h);
                    hora.setDiaSubtarea(ds);
                    horaService.guardar(hora);
                    ds.getHoras().add(hora);
                }
            }
        }
    }

    @Override
    @Transactional
    public void eliminarDiasYHorasPorSubtarea(Subtarea subtarea, Dia dia) {
        if (subtarea == null || subtarea.getId() == null) {
            throw new IllegalArgumentException("Subtarea no puede ser nula");
        }

        Optional<DiaSubtarea> diaSubOpt = subtarea.getDiaSubtareas().stream()
                .filter(ds -> ds.getDia().getId() == dia.getId())
                .findFirst();

        if (diaSubOpt.isPresent()) {
            DiaSubtarea ds = diaSubOpt.get();

            // Borrar las horas de ese día
            if (ds.getHoras() != null && !ds.getHoras().isEmpty()) {
                horaService.eliminarTodas(ds.getHoras());
            }

            // Borrar el DiaSubtarea
            diaSubtareaRepository.delete(ds);

            // Quitar la referencia en memoria
            subtarea.getDiaSubtareas().remove(ds);
        }
    }

    @Override
    public void eliminarDiaDeSubtarea(Subtarea subtarea, DiasDeSemana dia) {
        if (subtarea == null || subtarea.getId() == null) {
            throw new IllegalArgumentException("Subtarea no puede ser nula");
        }
        diaSubtareaRepository.deleteBySubtareaAndDia(subtarea, dia);
    }


    @Override
    public List<DiasSubtareaDto> listarDiasYHorasPorSubtarea(String email) {
        List<Subtarea> subtareas = subtareaService.obtenerTodasPorUsuario(email);

        for (Subtarea s : subtareas) {
            List<DiaSubtarea> dias = diaSubtareaRepository.findBySubtareaId(s.getId());
            s.setDiaSubtareas(new HashSet<>(dias));
        }

        List<DiasSubtareaDto> dtos = subtareas.stream()
                .flatMap(s -> mapper.toDto(s).stream())
                .collect(Collectors.toList());

        return dtos;

    }

    @Override
    public DiaSubtarea guardar(DiaSubtarea diaSubtarea) {
        return diaSubtareaRepository.save(diaSubtarea);
    }

    @Override
    @Transactional
    public void agregarHorasADiaSubtarea(Integer idSubtarea, Integer idDia, List<Integer> nuevasHoras) {
        Subtarea subtarea = subtareaService.buscarPorId(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada: " + idSubtarea));

        Dia dia = diaService.obtenerPorId(idDia)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada: " + idDia));
        DiaSubtarea diaSubtarea = diaSubtareaRepository.findBySubtareaAndDia(subtarea, dia)
                .orElseGet(() -> {
                    DiaSubtarea nuevo = new DiaSubtarea();
                    nuevo.setSubtarea(subtarea);
                    nuevo.setDia(dia);
                    nuevo.setEstado(EstadoTarea.PENDIENTE);
                    nuevo.setHoras(new HashSet<>());
                    return diaSubtareaRepository.save(nuevo);
                });

        // Filtrar las horas nuevas que ya existen
        List<Integer> horasExistentes = diaSubtarea.getHoras().stream()
                .map(Hora::getHora)
                .toList();

        List<Hora> horasParaAgregar = nuevasHoras.stream()
                .filter(h -> !horasExistentes.contains(h))
                .map(h -> {
                    Hora hora = new Hora();
                    hora.setHora(h);
                    hora.setDia(dia);
                    hora.setDiaSubtarea(diaSubtarea);
                    return hora;
                }).toList();

        horaService.guardarTodas(horasParaAgregar);


        diaSubtarea.getHoras().addAll(horasParaAgregar);
    }


    @Transactional
    @Override
    public void agregarDiasSubtarea(Integer idSubtarea,
                                    List<DiasDeSemana> diasSemana,
                                    Integer idSemana) {
        Subtarea subtarea = subtareaRepository.findById(idSubtarea)
                .orElseThrow();

        List<Dia> dias = diaRepository.findBySemana_IdAndNombreDiaIn(idSemana, diasSemana);

        List<Integer> diaIds = dias.stream().map(Dia::getId).toList();

        Set<Integer> existentes = diaSubtareaRepository.findExistingDiaIdsBySubtareaAndDiaIds(idSubtarea, diaIds);

        List<DiaSubtarea> nuevos = new ArrayList<>();

        for (Dia dia : dias) {
            if (!existentes.contains(dia.getId())) {

                DiaSubtarea ds = new DiaSubtarea();
                ds.setDia(dia);
                ds.setSubtarea(subtarea);
                ds.setEstado(EstadoTarea.PENDIENTE);

                nuevos.add(ds);
            }
        }

        diaSubtareaRepository.saveAll(nuevos);
    }

}


