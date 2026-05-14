package dgtic.core.system.dto;

import jakarta.validation.constraints.NotNull;

public class DescripcionDto {

     @NotNull(message = "El idSubtarea de la descripción no puede ser null")
    private Integer id;
    private String descripcion;
}
