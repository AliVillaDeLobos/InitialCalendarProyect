package dgtic.core.system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

     @NotNull(message = "El idSubtarea del usuario es obligatorio")
    private Integer idUsuario;
     @NotNull(message = "El nombres es un campo obligatorio")
     @Size(min = 3, max = 50, message = "El nombre debe contener mínimo 3 y maximo 50 caracteres")
     @Pattern(regexp = ".*[a-zA-Z].*", message = "El nombre solo puede contener letras")
    private String nombre;
     @NotNull(message = "El apellido paterno es un campo obligatorio")
     @Size(min = 3, max = 50, message = "El nombre debe contener mínimo 3 y maximo 50 caracteres")
     @Pattern(regexp = ".*[a-zA-Z].*", message = "El nombre solo puede contener letras")
    private String apellidoPaterno;
     @NotNull(message = "El apellido materno es un campo obligatorio")
     @Size(min = 3, max = 50, message = "El nombre debe contener mínimo 3 y maximo 50 caracteres")
     @Pattern(regexp = ".*[a-zA-Z].*", message = "El nombre solo puede contener letras")
    private String apellidoMaterno;
     @Email(message = "Debe intruducir un formato de correo electronico valido")
     @NotBlank(message = "El correo es obligatorio")
    private String email;
     @NotBlank(message = "La contraseña es obligatoria")
     @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,12}$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, incluir al menos una mayúscula, " +
                    "una minúscula y un número")
    private String password;

     private List<ClaseTareaDto> claseTareas;
}
