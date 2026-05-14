package dgtic.core.security.config;

import dgtic.core.security.model.UserDetailsImpl;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserInfo {
    private final UsuarioService usuarioService;

    public GlobalUserInfo(UsuarioService usuarioService) {this.usuarioService = usuarioService;}


    @ModelAttribute
    public void addUserInfoToModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            Usuario usuario =  usuarioService.findByEmail(authentication.getName()).orElseThrow(
                    () -> new ResourceNotFoundException("Usuario no encontrado"));
            model.addAttribute("usuarioId", usuario.getIdUsuario());
            model.addAttribute("usuarioNombre", usuario.getNombreCompleto());
            model.addAttribute("email", usuario.getEmail());
        }
    }

}
