package dgtic.core.system.controller.inicio;

import dgtic.core.system.dto.SubtareasEliminadasDto;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.SubtareaEliminada;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.service.SubtareaEliminadaService;
import dgtic.core.system.service.SubtareaService;
import dgtic.core.system.service.UsuarioService;
import dgtic.core.system.util.RenderPagina;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/admin/usuario")
public class UsuarioController {
    private UsuarioService usuarioService;
    private SubtareaService subtareaService;
    private SubtareaEliminadaService subtareaEliminadaService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, SubtareaService subtareaService,
                             SubtareaEliminadaService subtareaEliminadaService) {
        this.usuarioService = usuarioService;
        this.subtareaService = subtareaService;
        this.subtareaEliminadaService = subtareaEliminadaService;
    }


    @PostMapping("/modificar-usuario/{idUsuario}")
    public String modificarUsuario(@PathVariable("idUsuario")Integer id,
                                   @Valid @ModelAttribute Usuario usuario,
                                   BindingResult result,
                                   @RequestParam(name = "nuevaPassword", required = false)String nuevaPassword,
                                   Authentication auth,  Model model) {
        Usuario usr = usuarioService.findByEmail(auth.getName()).orElse(null);
        if(result.hasErrors()) {
            model.addAttribute("usuario", usr);
            model.addAttribute("error", "Verifica bien los campos, todos son obligatorios");
            return "usuarios/modificar-usuario";
        }
        Usuario acutlizado = usuarioService.update(id, usuario, nuevaPassword);
        model.addAttribute("titulo", "Modificar datos del usuario. ");
        model.addAttribute("success", "Usuario actualizado con éxito");
        model.addAttribute("usuario", acutlizado);
        return "/usuarios/modificar-usuario";
    }

    @GetMapping("/ver-usuario")
    public String verUsuario(Authentication auth, Model model) {
        Usuario usr = usuarioService.findByEmail(auth.getName()).orElseThrow(
                () -> new ResourceNotFoundException("Usuario no encontrado"));
        model.addAttribute("titulo", "Usuario" + usr.getNombre());
        model.addAttribute("usuario", usr);
        return "/usuarios/modificar-usuario";
    }

    @GetMapping(value = "/ver-subtareas-eliminadas")
    public String verSubtareasEliminadas(@RequestParam(name = "page", defaultValue = "0")int page,
                                         Authentication auth, Model model) {
        String email = auth.getName();
        Pageable pageable = PageRequest.of(page, 5);

        List<SubtareaEliminada> eliminadas = subtareaEliminadaService.todasPorUsuario(email).stream().toList();
        List<Integer> subtareaIds = eliminadas.stream()
                .map(se -> se.getSubtarea().getId())
                .toList();

        List<Subtarea> subtareas = subtareaService.obtenerPorIds(subtareaIds);

        Page<SubtareasEliminadasDto> eliminadasDtos =
                subtareaEliminadaService.subtareasEliminadas(pageable, subtareas, email);

        RenderPagina<SubtareasEliminadasDto> renderPagina = new RenderPagina<>("ver-subtareas-eliminadas", eliminadasDtos);
        model.addAttribute("subtareas",eliminadasDtos);
        model.addAttribute("page", renderPagina);
        model.addAttribute("titulo", "Lista de subtareas por nombre");
        return "/usuarios/ver-subtareas-eliminadas";
    }

    @PostMapping(value = "/restaurar-eliminada")
    public String restaurar(@RequestParam("idSubtareaEliminada")Integer id,
                            RedirectAttributes redirectAttributes){
        try {

            subtareaEliminadaService.restaurarSubtarea(id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al restaurar subtarea");
        }
        redirectAttributes.addFlashAttribute("success", "La tarea se restauro correctamente");
        return "redirect:/admin/usuario/ver-subtareas-eliminadas";
    }
}
