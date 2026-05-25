package dgtic.core.system.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "usuarios")
public class Usuario {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
     @NotNull(message = "El campo nombre es requerido")
    private String nombre;
     @NotNull(message = "El primer apellido es requerido")
    private String apellidoPaterno;
     @NotNull(message = "El segundo apellido es requerido")
    private String apellidoMaterno;
     @NotNull(message = "El correo no puede ir vavcio")
     @Email(message = "Correo no valido")
     @Column(name = "correo")
    private String email;
     @NotNull(message = "Contraseña necesaria")
     @Column(name = "password_hash")
    private String usuarioPassword;

     @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
     @ToString.Exclude
    List<ClaseTarea> claseTareas;

    public String getNombreCompleto () {return this.nombre + " " + this.apellidoPaterno;}

//    Traigo los roles directamente, y se cargan en la tabla usuarios_rol
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )

    private Collection<Rol> roles;


}
