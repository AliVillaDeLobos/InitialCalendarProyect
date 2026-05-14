package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"diaSubtareas"})
@EqualsAndHashCode(exclude = {"diaSubtareas"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subtareas")
public class Subtarea {
    //Recuerda hacer el filtrado cuando des una lista de Subtareas y no mandar las subtareas
    // que esten eliminadas

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_subtarea")
    private Integer id;
    private String nombre;
//    Se deja primitivo para no aceptar null
    private Boolean estado = false; /* Represneta el estado diario que va a tener la tarea, cuando se realice a
    diferencia del esta más global que contiene DiaTarea como enum, esto solo sirve de referencia si se
    llega a marcar true la tarea estara "finalizada" una especie de checklist*/

    //RECORDAR CAMBIARLO A TRUE CUANDO SE ELIMINE
    private Boolean eliminada = false; //esto indica si se mando a la tabla para eliminar
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
     @ManyToOne
     @JoinColumn(name = "id_tarea", nullable = false)
    private Tarea tarea;
     @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     @JoinColumn(name = "id_descripcion", nullable = true)
    private Descripcion descripcion;

     //Para el formulario de subtarea
    @Transient
    private String textoDescripcion;

     //Para acceder a los datos de DiaSubtarea
     @OneToMany(mappedBy = "subtarea", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DiaSubtarea> diaSubtareas = new HashSet<>();

}
