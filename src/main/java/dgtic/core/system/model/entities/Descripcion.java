package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "descripciones")
public class Descripcion {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_descripcion")
    private Integer id;
     @Column(name = "descripcion")
    private String texto;

}
