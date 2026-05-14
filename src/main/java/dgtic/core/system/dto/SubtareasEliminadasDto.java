package dgtic.core.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtareasEliminadasDto {


    @NotNull(message = "El idSubtarea de la subtarea es obligatorio.")
    private Integer idSubtareaEliminada;
    @NotBlank(message = "Es obligatorio el nombre del la subtarea")
    private String nombre;
    private LocalDate fechaEliminacion;
    @NotBlank(message = "Es obligatorio el nombre de la tarea a la que pertenece la subtarea")
    private String nombreTarea;
    @NotNull
    private  Boolean estado;
    private String mensaje;

}
