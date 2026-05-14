package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "subtarea_eliminada")
public class SubtareaEliminada {
    //Recuerda hacer el filtrado cuando des una lista de Subtareas y no mandar las subtareas
    // que esten eliminadas

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_subtarea_eliminada")
    private Integer id;
     @Column(name = "fecha_eliminacion", nullable = false)
    private LocalDate fechaEliminacion;
     @Column(name = "mensaje")
    private String mensaje;
     @OneToOne
     @JoinColumn(name = "id_subtarea", nullable = false, unique = true)
    private Subtarea subtarea;

}
