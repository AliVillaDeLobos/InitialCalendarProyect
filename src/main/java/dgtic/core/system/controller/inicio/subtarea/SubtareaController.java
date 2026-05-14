package dgtic.core.system.controller.inicio.subtarea;

import dgtic.core.system.dto.SubtareaDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.*;
import dgtic.core.system.service.*;
import dgtic.core.system.util.RenderPagina;
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

@Controller
@RequestMapping("/auth/subtareas")
public class SubtareaController {

    private final SubtareaService subtareaService;
    private final DescripcionService descripcionService;
    private final TareaService tareaService;
    private final SemanaService semanaService;

    public SubtareaController(SubtareaService subtareaService, DescripcionService descripcionService, TareaService tareaService, DescripcionServiceImpl descripcionServiceImpl, SemanaService semanaService) {
        this.subtareaService = subtareaService;
        this.descripcionService = descripcionService;
        this.tareaService = tareaService;
        this.semanaService = semanaService;
    }

//    Agregar un lista especifica que busque por la tarea, que nos made a otra pagina, similar a
//    esta que da todo general


    @GetMapping(value = "ver-lista-subtareas")
    public String verListaSubareas(@RequestParam(name = "page", defaultValue = "0")int page,
                                   Authentication auth, Model model ) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);
        Page<SubtareaDto> subtareas = subtareaService.todasSubtareaUsuario(email ,pageable);
        RenderPagina<SubtareaDto> renderPagina = new RenderPagina<>("ver-lista-subtareas", subtareas);
        model.addAttribute("subtareas", subtareas); //NO se necesita gteContent()
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de subtareas");
        return "subtareas/ver-subtareas";
    }

//    Aqui implemente el Pagable desde la base de datos
    @PostMapping(value = "recibir-nombre")
    public String recibirNombre(@RequestParam(value = "nombre") String nombre,
                               @RequestParam(name = "page", defaultValue = "0")int page,
                               Authentication auth,
                               Model model) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);
        Page<SubtareaDto> subtareas = subtareaService.todasPorUsuarioYNombre(email, nombre, pageable);
        RenderPagina<SubtareaDto> renderPagina = new RenderPagina<>("ver-lista-subtareas", subtareas);
        model.addAttribute("subtareas",subtareas);
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de subtareas por nombre");
        subtareas.getContent().forEach(subtarea -> System.out.println(subtarea.getCodigoColor()));
        return "subtareas/ver-subtareas";
    }

    @GetMapping("crear-subtarea")
    public String crearSubtarea(Model model, Authentication auth) {
        model.addAttribute("titulo", "Agrega la subtarea que desea crear");
        String email = auth.getName();
        List<Tarea> disponibles = tareaService.findTareasUsuarioCollection(email).stream().toList();
        model.addAttribute("subtarea", new Subtarea());
        model.addAttribute("tareas", disponibles);
        model.addAttribute("descripcion", new Descripcion());
        model.addAttribute("contenido", "Para el nombre se sugiere usar uno descriptio de la actividad a realizas. " +
                "\nPor ejemplo 'Leer 20 pag. del libro' o 'Realizar diapositivas del proyecto de matemáticas'.");
        return "subtareas/crear-subtarea";
    }

    @PostMapping(value = "guardar-subtarea")
    public String guardarSubtarea(@Valid @ModelAttribute("subtarea") Subtarea subtarea,
                                  BindingResult result, Model model,
                                  Authentication auth, RedirectAttributes redirect) {
        String email = auth.getName();
        if(result.hasErrors()) {
            List<Tarea> disponibles = tareaService.findTareasUsuarioCollection(email).stream().toList();
            model.addAttribute("tareas", disponibles);
            model.addAttribute("subtarea", subtarea);
            model.addAttribute("descripcion", new Descripcion());
            model.addAttribute("error", "Verifica bien los campos, todos son obligatorios");
            return "subtareas/crear-subtarea";
        }
        Tarea tarea = tareaService.findByIdAndUsuario(subtarea.getTarea().getIdTarea(), email).orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrado"));
        subtarea.setTarea(tarea);

        if(subtarea.getTextoDescripcion() != null && !subtarea.getTextoDescripcion().isEmpty()) {
            Descripcion descripcion = new Descripcion();
            descripcion.setTexto(subtarea.getTextoDescripcion());
            descripcionService.save(descripcion);
            subtarea.setDescripcion(descripcion);
        }

        subtareaService.guardar(subtarea);
        model.addAttribute("mensaje", "Clase tarea guardada con éxito. Ya la puedes checar en tu lista de clase tareas");
        redirect.addFlashAttribute("success", "Clase tarea creada con éxito");
        model.addAttribute("subtarea", new Subtarea());
        return "redirect:/auth/subtareas/ver-lista-subtareas";
    }

    @PostMapping(value = "modificar-subtarea")
    public String modificarSubtarea(@Valid @ModelAttribute("subtarea") Subtarea subtarea,
                                  BindingResult result, Model model,
                                  Authentication auth, RedirectAttributes redirect,
                                  @RequestParam("idTarea") Integer idTarea) {
            String email = auth.getName();
            if(result.hasErrors()) {
            List<Tarea> disponibles = tareaService.findTareasUsuarioCollection(email).stream().toList();
            model.addAttribute("tareas", disponibles);
            model.addAttribute("subtarea", subtarea);
            model.addAttribute("descripcion", subtarea.getDescripcion());
            model.addAttribute("error", "Verifica bien los campos, todos son obligatorios");
            return "subtareas/crear-subtarea";
        }
        Tarea tarea = tareaService.findByIdAndUsuario(idTarea, email).orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrado"));
        subtarea.setTarea(tarea);

        if(subtarea.getDescripcion() != null) {
            descripcionService.save(subtarea.getDescripcion());
        }

        subtareaService.modificar(subtarea);
        model.addAttribute("mensaje", "Clase tarea guardada con éxito. Ya la puedes checar en tu lista de clase tareas");
        redirect.addFlashAttribute("success", "Clase tarea creada con éxito");
        model.addAttribute("subtarea", new Subtarea());
        return "redirect:/auth/subtareas/ver-lista-subtareas";
    }

    @GetMapping(value = "cambiar-estado-subtarea/{idSubtarea}")
    public String cambiarEstadoSubtarea(@PathVariable("idSubtarea")Integer id) {
        subtareaService.actualizarEstado(id);
        return "redirect:/auth/subtareas/ver-lista-subtareas";
    }

    @GetMapping(value = "modificar-subtarea/{idSubtarea}")
    public String modificarSubtarea(@PathVariable("idSubtarea")Integer id, Model model, Authentication auth) {
        Subtarea subtarea = subtareaService.buscarPorId(id).orElseThrow(
                () -> new ResourceNotFoundException("Clase Tarea con idSubtarea: " +id+ "\nNo encontrado"));
        String email = auth.getName();        List<Tarea> disponibles = tareaService.findTareasUsuarioCollection(email).stream().toList();
        Descripcion descripcion = descripcionService.findById(subtarea.getDescripcion().getId()).orElseThrow(
                () -> new ResourceNotFoundException("No se encontro el ide de la descripcioón: " + subtarea.getDescripcion().getId()));
        subtarea.setTextoDescripcion(descripcion.getTexto());
        model.addAttribute("tareas", disponibles);
        model.addAttribute("subtarea", subtarea);
        model.addAttribute("titulo", "Modificar subtarea");
        return "subtareas/crear-subtarea";
    }

    @PostMapping(value = "eliminar-subtarea")
    public String eliminarSubtarea(@RequestParam("idSubtarea")Integer id,
                                   @RequestParam("mensaje")String mensaje,
                                   RedirectAttributes redirectAttributes,
                                   Authentication auth) {
        String mensajeRegreso = subtareaService.eliminar(id, mensaje +"\nUsuario con rol: " + auth.getAuthorities());
        redirectAttributes.addFlashAttribute("success", mensajeRegreso);
        return "redirect:/auth/subtareas/ver-lista-subtareas";
    }


}
