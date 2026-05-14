package dgtic.core.system.controller.inicio;

import dgtic.core.system.dto.ClaseTareaDto;
import dgtic.core.system.mapper.ClaseTareaMapper;
import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.enums.Color;
import dgtic.core.system.service.ClaseTareaService;
import dgtic.core.system.service.TareaService;
import dgtic.core.system.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import dgtic.core.system.util.RenderPagina;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth/tareas")
public class TareasController {
    private final TareaService tareasService;
    private final ClaseTareaService claseTareaService;
    private final ClaseTareaMapper claseTareaMapper;

    @Autowired
    public TareasController(TareaService tareasService, ClaseTareaService claseTareaService, ClaseTareaMapper claseTareaMapper) {
        this.tareasService = tareasService;
        this.claseTareaService = claseTareaService;
        this.claseTareaMapper = claseTareaMapper;
    }

    @GetMapping(value = "ver-lista-tareas")
    public String verListaTareas(@RequestParam(name = "page", defaultValue = "0")int page,
                                 Authentication auth , Model model ) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);
        Page<Tarea> tareas = tareasService.findTareasUsuario(email ,pageable);
        RenderPagina<Tarea> renderPagina = new RenderPagina<>("ver-lista-tareas", tareas);
        model.addAttribute("tareas", tareas);
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de tareas");
        return "tareas/ver-tareas";
    }

    @PostMapping(value = "recibir-color")
    public String recibirColor(@RequestParam(value = "color")Color color,
                               @RequestParam(name = "page", defaultValue = "0")int page,
                               Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);
        Page<Tarea> tareas = tareasService.buscarPorColorYEmailP(color, email , pageable);
        RenderPagina<Tarea> renderPagina = new RenderPagina<>("ver-lista-tareas", tareas);
        model.addAttribute("tareas", tareas);
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de tareas por color");
        return "tareas/ver-tareas";
    }

    @GetMapping("crear-tarea")
    public String crearTarea(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        model.addAttribute("titulo", "Agrega la tarea que desea crear");
        String email = auth.getName();
        List<ClaseTareaDto> listaUsados = claseTareaService.findAllByUsuarioEmail(email)
                .stream().map(claseTareaMapper::toDto).collect(Collectors.toList());
        model.addAttribute("claseTarea", listaUsados);
        Tarea tarea = new Tarea();
        tarea.setClaseTarea(new ClaseTarea());
        model.addAttribute("tarea", tarea);
        model.addAttribute("contenido", "Para el nombre se sugiere usar lo más general posible de acuerdo al objetivo");
        redirectAttributes.addFlashAttribute("success", "Tarea creada con éxito.");
        return "tareas/crear-tarea";
    }

    @PostMapping(value = "guardar-tarea")
    public String guardarTarea(@Valid @ModelAttribute("tarea")Tarea tarea, BindingResult result, Model model,
                             Authentication auth) {
        String email = auth.getName();
        if(result.hasErrors()) {
            List<ClaseTareaDto> listaUsados = claseTareaService.findAllByUsuarioEmail(email)
                    .stream().map(claseTareaMapper::toDto).collect(Collectors.toList());
            model.addAttribute("claseTarea", listaUsados);
            tarea.setClaseTarea(new ClaseTarea());
            model.addAttribute("tarea", tarea);
            model.addAttribute("error", "Verifica bien los campos, todos son obligatorios");
            return "tareas/crear-tarea";
        }
        tareasService.save(tarea);

        model.addAttribute("mensaje", "Tarea guardada con éxito. Ya la puedes checar en tu lista de tareas");
        model.addAttribute("success", "Tarea creada con éxito");
        model.addAttribute("tarea", new Tarea());
        return "redirect:/auth/tareas/ver-lista-tareas";
    }

    @GetMapping(value = "modificar-tarea/{id}")
    public String modificarTarea(@PathVariable("id")Integer id, Model model, Authentication auth) {
        Tarea tarea = tareasService.findById(id).orElse(null);
        String email = auth.getName();
        List<ClaseTareaDto> listaUsados = claseTareaService.findAllByUsuarioEmail(email)
                .stream().map(claseTareaMapper::toDto).collect(Collectors.toList());
        model.addAttribute("claseTarea", listaUsados);
        tarea.setClaseTarea(new ClaseTarea());
        model.addAttribute("tarea", tarea);
        model.addAttribute("titulo", "Modificada tarea");
        return "tareas/crear-tarea";
    }


    @GetMapping(value = "eliminar-tarea/{id}")
    public String eliminarTarea(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes) {
        tareasService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Tarea eliminada con éxito");
        return "redirect:/auth/tareas/ver-lista-tareas";
    }



}
