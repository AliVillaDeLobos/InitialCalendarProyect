package dgtic.core.system.model.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tareas")
public class Tarea {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_tarea")
    private Integer idTarea;
      @NotNull(message = "El nombre es obligatorio para crear la tarea")
    private String nombre;

     @ManyToOne
     @JoinColumn(name = "id_clase_tarea", nullable = false)
     @ToString.Exclude
     @NotNull(message = "Se le tiene que asignar un Tipo de Tarea ")
    private ClaseTarea claseTarea;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtarea> subtareas;

}
