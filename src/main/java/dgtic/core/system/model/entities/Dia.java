package dgtic.core.system.model.entities;

import dgtic.core.system.convert.DiasSemanaConverter.DiasDeSemanaConverter;
import dgtic.core.system.model.enums.DiasDeSemana;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "dias")
public class Dia {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_dia")
    private Integer id;
    private LocalDate fecha;
     @Column(nullable = false)
     @Convert(converter = DiasDeSemanaConverter.class)
    private DiasDeSemana nombreDia;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "id_semana", nullable = false)
     @ToString.Exclude
    private Semana semana;
}
