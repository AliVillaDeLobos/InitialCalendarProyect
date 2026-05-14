package dgtic.core.system.model.entities;

import dgtic.core.system.model.enums.EstadoTarea;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "periodo_tarea")
public class PeriodoTarea {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_periodo_tarea")
    private Integer id;
    private LocalDate fechaCreacion;
    private LocalDate fechaFin;
     @Enumerated(EnumType.STRING)
    private EstadoTarea estado;
     @ManyToOne
     @JoinColumn(name = "id_tarea", nullable = false)
     @ToString.Exclude
    private Tarea tarea;

}
