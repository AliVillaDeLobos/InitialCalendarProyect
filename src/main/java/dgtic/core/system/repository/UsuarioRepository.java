package dgtic.core.system.repository;

import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.entities.Usuario;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAllByOrderByNombreDesc();
    List<Usuario> findByApellidoPaternoContaining(String apellidoPaterno);

//    Consultas Nativas

    @Query(value = """
            SELECT u FROM Usuario u
            WHERE u.nombre LIKE %:frag%
            OR u.apellidoPaterno LIKE %:frag%
            OR u.apellidoMaterno LIKE %:frag%
            """)
    List<Usuario> encontrarPorFragmento(String frag);


    @Query(value = """
            SELECT u.usuarioPassword FROM Usuario u
            WHERE u.email = :email
            """)
    String encontrarContraseñaPorEmail(String email);


    @Query(value = """
            SELECT u FROM Usuario u
            WHERE u.email = :email
            AND u.usuarioPassword = :password
            """)
    Optional<Usuario> encontrarUsaruarioPorEmailYContraseña(String email, String password);

    
    
//  Consultas con Relaciones
    // Sé que no debería ir aquí porque regresa tareas pero ya no se me ocurría nada para agregar

    @Query(value = """
            SELECT  t.claseTarea.color FROM Tarea t
            WHERE t.claseTarea.usuario.email = :email
            """)
    List<String> encontrarTop2DelColorClaseTarea(String email, PageRequest page);

    @Query(value = """
            SELECT t FROM Tarea t
            WHERE t.claseTarea.usuario.email = :email
            AND t.claseTarea.color = :color
            """)
    List<Tarea> encontrarTareasPorTipoTareaYEmail(String email, String color);


    @Query(value = """
            SELECT SIZE(u.claseTareas) FROM Usuario u
            WHERE u.email = :email
            """)
    Integer numeroDeClaseTareasPorEmail(String email);



}
