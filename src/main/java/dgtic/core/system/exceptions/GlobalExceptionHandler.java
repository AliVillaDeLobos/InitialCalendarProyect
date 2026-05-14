package dgtic.core.system.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ModelAndView view(HttpStatus status, String mensaje, Map<String, String> fields) {
        ModelAndView mv = new ModelAndView("error/general");
        mv.addObject("status", status.value());
        mv.addObject("error", status.getReasonPhrase());
        mv.addObject("mensaje", mensaje);
        mv.addObject("timestamp", Instant.now().toString());
        mv.addObject("fields", fields);
        return mv;
    }

    private ModelAndView view(HttpStatus status, String mensaje) {
        return view(status, mensaje, null);
    }

    // 404: Recurso no encontrado (por ejemplo, controlador inexistente)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        return view(HttpStatus.NOT_FOUND, "El recurso solicitado no existe.");
    }

    // 400: Parámetros inválidos (validaciones @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        return view(HttpStatus.BAD_REQUEST, "Errores de validación en el formulario", errores);
    }

    // 400: Violaciones de restricciones
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errores = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage()));
        return view(HttpStatus.BAD_REQUEST, "Parámetros inválidos", errores);
    }

    // 400: Tipo de parámetro incorrecto
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Tipo inválido para '" + ex.getName() + "'. Esperado: " +
                (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");
        return view(HttpStatus.BAD_REQUEST, msg);
    }

    // 405: Método no permitido (GET/POST incorrecto)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return view(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no permitido para esta ruta.");
    }

    // 409: Violaciones de integridad (FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrity(DataIntegrityViolationException ex) {
        return view(HttpStatus.CONFLICT, "Violación de integridad de datos en la base de datos.");
    }

    // 400: Argumentos ilegales
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex) {
        return view(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 500: Error interno genérico
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex) {
        return view(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + ex.getMessage());
    }
}
