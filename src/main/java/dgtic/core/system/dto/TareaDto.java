package dgtic.core.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TareaDto {

     @NotNull(message = "Es obligatorio el idSubtarea de la tarea")
    private Integer idTarea;
     @NotNull(message = "El campo del nombre no puede ser null")
     @Size(min = 3, max = 50, message = "El nombre debe contener mínimo 3 y maximo 50 caracteres")
     @Pattern(regexp = ".*[a-zA-Z].*", message = "El nombre solo puede contener letras")
    private String nombre;

    private ClaseTareaDto claseTarea;
    private List<SubtareaDto> subtareas;

}
