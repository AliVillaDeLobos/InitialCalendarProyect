package dgtic.core.system.controller;

import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PrincipalController {
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping("/")
    public String inicio(Model model) {
        model.addAttribute("mensaje", "¡Bienvenido al Calendario!");
        model.addAttribute("valor", "Por favor inicia sesión si ya cuentas con una, sino creala");
        model.addAttribute("usuarioLogin", new Usuario());
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @PostMapping("/registrar")
    public String register(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("error", "Verifica los errores de los siguientes campos");
            model.addAttribute("usuarioLogin", new Usuario());
            model.addAttribute("mostrarRegistro", true);
            return "index";
        }

        if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("usuario", usuario);
//            model.addAttribute("mensaje", "Ya existe un usuario con ese email");
            model.addAttribute("mostrarRegistro", true);
            model.addAttribute("usuarioLogin", new Usuario());
            redirectAttributes.addFlashAttribute("error", "Ya existe un usuario con ese email");
            return "index";
        }
        usuarioService.save(usuario);

        model.addAttribute("mensaje", "Usuario registrado con éxito.\nYa puede acceder con su email y contraseña");
        redirectAttributes.addFlashAttribute("success", "Usuario registrado con éxito");
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarioLogin", new Usuario());
        return "redirect:/";
    }


    @PostMapping("/procesar-login")
    public String login(@ModelAttribute Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        Usuario usr = usuarioService.findByEmail(usuario.getEmail()).orElse(null);
        if (usr == null) {
            redirectAttributes.addFlashAttribute("error", "Verifique su email. No se encuentra registrado");
            model.addAttribute("mostrarLogin", true);
            return "index";
        }
        if (!usr.getUsuarioPassword().equals(usuario.getUsuarioPassword())) {
            model.addAttribute("error", "La contraseña no corresponde");
            model.addAttribute("mostrarLogin", true);
            return "index";
        }
        model.addAttribute("success", "Ingresaste con éxito");
        return "redirect:/auth/inicio";
    }

}
