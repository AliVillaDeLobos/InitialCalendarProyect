package dgtic.core.system.model.entities;

import dgtic.core.system.convert.DiasSemanaConverter.EstadoTareaConverter;
import dgtic.core.system.model.enums.EstadoTarea;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"subtarea", "horas"}) // en DiaSubtarea
@EqualsAndHashCode(exclude = {"subtarea", "horas"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dia_subtarea")
public class DiaSubtarea {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_dia_subtarea")
    private Integer id;
     @Enumerated(EnumType.STRING)
     @Convert(converter = EstadoTareaConverter.class)
     //Este informa el estado 'pendiente' 'finalizada/completada', 'cancelada'
    private EstadoTarea estado;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "id_dia", nullable = false)
    private Dia dia;
     @ManyToOne
     @JoinColumn(name = "id_subtarea", nullable = false)
    private Subtarea subtarea;
    @OneToMany(mappedBy = "diaSubtarea",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Hora> horas = new HashSet<>();
}
