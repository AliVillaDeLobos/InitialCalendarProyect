package dgtic.core.system.controller.inicio;

import dgtic.core.system.dto.DiasSubtareaDto;
import dgtic.core.system.exceptions.IncorrectAccessException;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.service.DiasSubtareaService;
import dgtic.core.system.service.SubtareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


@Controller
@RequestMapping("/auth/inicio")
public class InicioController {
    private final DiasSubtareaService diasSubtareaService;
    private final SubtareaService subtareaService;

    @Autowired
    public InicioController(DiasSubtareaService diasSubtareaService, SubtareaService subtareaService) {
        this.diasSubtareaService = diasSubtareaService;
        this.subtareaService = subtareaService;
    }


    @GetMapping()
    public String inicio(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("titulo", "Inicio");
        List<DiasSubtareaDto> dtos = diasSubtareaService.listarDiasYHorasPorSubtarea(email);
        model.addAttribute("diasSubtareas", dtos);
        return "principal/inicio";
    }

    @PostMapping("/seleccionar-subtarea")
    public String seleccionarSubtarea(@RequestParam("idSubtarea") Integer idSubtarea, Authentication auth, Model model) {
        String email = auth.getName();

        List<DiasSubtareaDto> dtos = diasSubtareaService.listarDiasYHorasPorSubtarea(email);
        model.addAttribute("diasSubtareas", dtos);
        model.addAttribute("titulo", "Inicio");

        Subtarea subtarea = subtareaService.buscarPorId(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada"));

        model.addAttribute("subtareaSeleccionada", subtarea);
        model.addAttribute("tareaSeleccionada", subtarea.getTarea());

        model.addAttribute("subtareasRelacionadas", subtarea.getTarea().getSubtareas());
        return "principal/inicio";
    }

/*   @GetMapping("/detalle-subtarea/{idSubtarea}")
    @ResponseBody
    public DiasSubtareaDto obtenerDetalleSubtarea(@PathVariable Integer idSubtarea, Authentication auth) {
        String email = auth.getName();
        Subtarea subtarea = subtareaService.buscarPorId(idSubtarea)
                .orElseThrow(() -> new ResourceNotFoundException("Subtarea no encontrada"));

        if (!subtarea.getTarea().getClaseTarea().getUsuario().getEmail().equals(email)) {
            throw new IncorrectAccessException("No puedes ver esta subtarea");
        }

        List<DiasSubtareaDto> dtos = diasSubtareaService.listarDiasYHorasPorSubtarea(email);
        List<Subtarea> subtareas = subtareaService.

        DiasSubtareaDto dto = new DiasSubtareaDto();
        dto.setSubtarea(subtarea);
        dto.setTarea(subtarea.getTarea());
        dto.setSubtareasRelacionadas(subtarea.getTarea().getSubtareas());
        return dto;
    }*/


}
