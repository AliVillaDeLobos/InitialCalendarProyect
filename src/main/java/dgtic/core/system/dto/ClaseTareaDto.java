package dgtic.core.system.dto;

import dgtic.core.system.model.enums.Color;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseTareaDto {
     @NotNull(message = "El idSubtarea de la clase tarea es obligatorio")
    private Integer idClaseTarea;
     @NotNull(message = "El color es obligatorio")
    private Color color;
     @NotNull(message = "El nombre es obligatorio")
    private String nombre;
     @NotNull(message = "ES necesario asignarlo a un usuario por su idSubtarea")
    private Integer idUsuario;

    private  List<TareaDto> tareas;

}
