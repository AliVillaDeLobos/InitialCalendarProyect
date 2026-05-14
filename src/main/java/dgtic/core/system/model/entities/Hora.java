package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hora")
public class Hora {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHora;

     @Range(min = 0, max = 23)
    private Integer hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_subtarea", nullable = false)
    private DiaSubtarea diaSubtarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia", nullable = false)
    private Dia dia;
}
