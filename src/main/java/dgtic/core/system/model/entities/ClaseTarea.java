package dgtic.core.system.model.entities;

import dgtic.core.system.convert.ColorConverter;
import dgtic.core.system.model.enums.Color;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "claseTarea")
public class ClaseTarea {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_clase_tarea")
    private Integer idClaseTarea;
     @Convert(converter = ColorConverter.class)
    private Color color;
    private String nombre;

     @ManyToOne
     @JoinColumn(name = "id_usuario", nullable = false)
     @ToString.Exclude
    private Usuario usuario;

     @OneToMany
     @JoinColumn(name = "id_tarea", nullable = true)
     @ToString.Exclude
    private List<Tarea> tareas;


}
