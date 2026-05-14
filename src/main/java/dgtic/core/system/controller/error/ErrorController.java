package dgtic.core.system.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object messageObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = (statusObj != null) ? Integer.parseInt(statusObj.toString()) : 500;
        String mensaje = (messageObj != null) ? messageObj.toString() : "Error interno del servidor";

        model.addAttribute("status", status);
        model.addAttribute("error", HttpStatus.valueOf(status).getReasonPhrase());
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("timestamp", LocalDateTime.now());

        return "error/general";
    }



        @GetMapping("/acceso-denegado")
        public String accesoDenegado() {
            return "error/acceso-denegado";
        }

}
