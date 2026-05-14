package dgtic.core.system.dto;

import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.enums.EstadoTarea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiasSubtareaDto {
    private Integer idDiaSubtarea;
    private Integer idSubtarea;
    private String nombreSubtarea;
    private Integer idSemana;
    private LocalDate fechaInicioSemana;
    private LocalDate fechaFinSemana;
    private Integer numeroSemana;
    private String nombreDia;
    private Collection<Integer> horas;
    private EstadoTarea estado;
    private String colorClase;
    private Tarea tarea;
    private Subtarea subtarea;

}
