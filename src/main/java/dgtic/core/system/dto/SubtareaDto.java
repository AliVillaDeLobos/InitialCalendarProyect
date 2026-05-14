package dgtic.core.system.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class SubtareaDto {

     @NotNull(message = "El idSubtarea de la subtarea es obligatorio.")
    private Integer idSubtarea;
     @NotBlank(message = "Es obligatorio el nombre del la subtarea")
    private String nombre;
     //Si es necesario el campo para maperalo a la entity
    private LocalDate fechaCreacion;
     @NotBlank(message = "Es obligatorio el nombre de la tarea a la que pertenece la subtarea")
    private String nombreTarea;
     @NotNull
    private Boolean eliminada;
     @NotNull
    private  Boolean estado;
     @NotNull
     private  String codigoColor;
    private List<DiasSubtareaDto> diasSubtareasDto;


    public SubtareaDto(Integer idSubtarea, String nombre, LocalDate fechaCreacion, String nombreTarea, Boolean eliminada, Boolean estado) {
        this.idSubtarea = idSubtarea;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.nombreTarea = nombreTarea;
        this.eliminada = eliminada;
        this.estado = estado;
    }
}
