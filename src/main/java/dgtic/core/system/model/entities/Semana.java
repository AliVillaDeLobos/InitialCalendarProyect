package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "semanas")
public class Semana {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_semana")
    private Integer id;
     @Column(nullable = false)
    private Integer numeroSemana;
     @Column(name = "anio", nullable = false)
    private Integer anioSemana;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Porsi quiero traer los datos de los días a la semana
    @OneToMany(mappedBy = "semana",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Dia> dias;
}
