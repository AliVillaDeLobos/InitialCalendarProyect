package dgtic.core.system.controller.inicio.subtarea;

import dgtic.core.system.dto.DiasSubtareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.*;
import dgtic.core.system.model.enums.DiasDeSemana;
import dgtic.core.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth/dias-horas")
public class DiasHorasController {
    private final SubtareaService subtareaService;
    private final SemanaService semanaService;
    private final DiasSubtareaService diasSubtareaService;
    private final HoraService horaService;
    private final DiaService diaService;

    @Autowired
    public DiasHorasController(SubtareaService subtareaService, SemanaService semanaService,
                               DiasSubtareaService diasSubtareaService, HoraService horaService, DiaService diaService) {
        this.subtareaService = subtareaService;
        this.semanaService = semanaService;
        this.diasSubtareaService = diasSubtareaService;
        this.horaService = horaService;
        this.diaService = diaService;
    }

    @GetMapping(value = "ver-lista")
    public String verListaSubtareas(Authentication auth, Model model) {
        String email = auth.getName();

        List<DiasSubtareaDto> dtos = diasSubtareaService.listarDiasYHorasPorSubtarea(email);
        model.addAttribute("diasSubtarea", dtos);
        model.addAttribute("titulo", "Lista de subtareas con días y horas");
        return "dias-horas/ver-subtareas-dias-horas";
    }

    @GetMapping(value = "agregar-dias-horas/{id}")
    public String mostrarAgregarDiasHoras(@PathVariable("id") Integer idSubtarea, Model model) {
        Subtarea subtarea = subtareaService.buscarPorId(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada"));
        DiaSubtarea diaSubtarea = new DiaSubtarea();
        diaSubtarea.setSubtarea(subtarea);

        Semana semana = semanaService.obtenerSemanaActual();
        List<Dia> diasSemana = diaService.obtenerDiasPorSemana(semana.getId());

        model.addAttribute("titulo", "Agregar Días y Horas");
        model.addAttribute("diaSubtarea", diaSubtarea);
        model.addAttribute("dias", diasSemana);
        model.addAttribute("idSubtarea", subtarea.getId()); // Lo mando directo porque en Thymeleaf no esta leyendo bien

        return "dias-horas/agregar-dias-horas";
    }

//    Metodo para hacer las horas dinamicas
    @GetMapping("horas-libres/{idDia}")
    @ResponseBody
    public List<Integer> obtenerHorasLibresPorDia(@PathVariable Integer idDia) {
        return horaService.obtenerHoraslibres(idDia);
    }



    @PostMapping("guardar")
    public String guardarDiasHoras(@RequestParam("idSubtarea") Integer idSubtarea,
                                   @RequestParam("idDia") Integer idDia,
                                   @RequestParam("horasSeleccionadas") List<Integer> horasSeleccionadas,
                                   RedirectAttributes redirect) {

        diasSubtareaService.agregarHorasADiaSubtarea(idSubtarea, idDia, horasSeleccionadas);

        redirect.addFlashAttribute("success", "Días y horas asignadas correctamente");
        return "redirect:/auth/dias-horas/ver-lista";
    }



    @PostMapping("eliminar-dia/{idSubtarea}/{nombreDia}")
    public String eliminarDiaDeSubtarea(@PathVariable Integer idSubtarea,
                                        @PathVariable(name = "nombreDia") String dia,
                                        RedirectAttributes redirect) {

        Subtarea subtarea = subtareaService.buscarPorId(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada: " + idSubtarea));
        DiasDeSemana diaEliminar;
        try {
            diaEliminar = DiasDeSemana.valueOf(dia.toUpperCase()); // convierte el String a Enum
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", "Día inválido: " + dia);
            return "redirect:/dias-horas/ver-lista";
        }
        diasSubtareaService.eliminarDiaDeSubtarea(subtarea, diaEliminar);

        redirect.addFlashAttribute("success", "Día " + diaEliminar + " eliminado correctamente");
        return "redirect:/auth/dias-horas/ver-lista";
    }


}
