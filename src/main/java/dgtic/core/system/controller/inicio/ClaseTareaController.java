package dgtic.core.system.controller.inicio;

import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.mapper.ClaseTareaMapper;
import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.model.enums.Color;
import dgtic.core.system.service.ClaseTareaService;
import dgtic.core.system.service.UsuarioService;
import dgtic.core.system.util.RenderPagina;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth/clase-tareas")
public class ClaseTareaController {
    private final ClaseTareaService claseTareaService;
    private final UsuarioService usuarioService;


/*      PODRIA USARLO EN LUAGR DE LLAMAR AL METODO EN TODOS LADOS
    private List<Color> obtenerColoresDisponiblesParaUsuario(HttpSession session) {
        return claseTareaService.obtenerColoresDisponibles(getUsuarioEmail(session)).stream().toList();
    }*/

    public ClaseTareaController(ClaseTareaService claseTareaService,
                                UsuarioService usuarioService) {
        this.claseTareaService = claseTareaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = "ver-lista-clase_tareas")
    public String verListaClaseTareas(@RequestParam(name = "page", defaultValue = "0")int page,
                                      Authentication auth, Model model ) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);
        Page<ClaseTarea> claseTareas = claseTareaService.findClaseTareasUsuario(email ,pageable);
        RenderPagina<ClaseTarea> renderPagina = new RenderPagina<>("ver-lista-clasetareas", claseTareas);
        model.addAttribute("claseTareas", claseTareas); //NO se necesita gteContent()
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de clase tareas");
        return "clase-tareas/ver-clase_tareas";
    }

    @PostMapping(value = "recibir-nombre")
    public String recibirColor(@RequestParam(value = "nombre") String nombre,
                               @RequestParam(name = "page", defaultValue = "0")int page,
                               HttpSession session,
                               Model model, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("usuarioEmail");
        Pageable pageable = PageRequest.of(page, 5);
        Page<ClaseTarea> claseTareas = claseTareaService.findClaseTareasNombreYUsuario(nombre, email , pageable);
        RenderPagina<ClaseTarea> renderPagina = new RenderPagina<>("ver-lista-tareas", claseTareas);
        model.addAttribute("claseTareas",claseTareas);
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de clase tareas por nombre");
        return "clase-tareas/ver-clase_tareas";
    }

    @GetMapping("crear-clase_tarea")
    public String crearClaseTarea(Model model, Authentication auth) {
        model.addAttribute("titulo", "Agrega la clase tarea que desea crear");
        String email = auth.getName();
        List<Color> disponibles = claseTareaService.obtenerColoresDisponibles(email).stream().toList();
        model.addAttribute("claseTarea", new ClaseTarea());
        model.addAttribute("colores", disponibles);
        model.addAttribute("contenido", "Para el nombre se sugiere usar un campo generico, como 'salud' o 'escuela'.");
        return "clase-tareas/crear-clase_tarea";
    }

    @PostMapping(value = "guardar-clase_tarea")
    public String guardarClaseTarea(@Valid @ModelAttribute("claseTarea")ClaseTarea clasetarea, BindingResult result, Model model,
                               Authentication auth, RedirectAttributes redirect) {
            String email = auth.getName();
        if(result.hasErrors()) {
            List<Color> disponibles = claseTareaService.obtenerColoresDisponibles(email).stream().collect(Collectors.toList());
            model.addAttribute("colores", disponibles);
            model.addAttribute("claseTarea", clasetarea);
            model.addAttribute("error", "Verifica bien los campos, todos son obligatorios");
            return "clase-tareas/crear-clase_tarea";
        }
        Usuario usuario = usuarioService.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        clasetarea.setUsuario(usuario);
        claseTareaService.save(clasetarea);
        model.addAttribute("mensaje", "Clase tarea guardada con éxito. Ya la puedes checar en tu lista de clase tareas");
        redirect.addFlashAttribute("success", "Clase tarea creada con éxito");
        model.addAttribute("claseTarea", new ClaseTarea());
        return "redirect:/auth/clase-tareas/ver-lista-clase_tareas";
    }

    @GetMapping(value = "modificar-clase_tarea/{id}")
    public String modificarClaseTarea(@PathVariable("id")Integer id, Model model, Authentication auth) {
        ClaseTarea claseTarea = claseTareaService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Clase Tarea con idSubtarea: " +id+ "\nNo encontrado"));
        String email = auth.getName();
        List<Color> disponibles = claseTareaService.obtenerColoresDisponibles(email).stream().collect(Collectors.toList());
        model.addAttribute("colores", disponibles);
        model.addAttribute("claseTarea", claseTarea);
        model.addAttribute("titulo", "Modificar clase tarea");
        return "clase-tareas/crear-clase_tarea";
    }

    @GetMapping(value = "eliminar-clase_tarea/{id}")
    public String eliminarClaseTarea(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes) {
            claseTareaService.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Tarea no encontrada con idSubtarea: " + id));
            claseTareaService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Clase tarea eliminada con éxito");
        return "redirect:/auth/clase-tareas/ver-lista-clase_tareas";
    }


}
