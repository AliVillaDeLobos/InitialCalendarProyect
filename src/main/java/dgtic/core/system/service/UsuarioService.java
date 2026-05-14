package dgtic.core.system.service;

import dgtic.core.system.model.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> getUsuarios();
    Optional<Usuario> findById(Integer id);
    void delete(Integer id);
    Usuario update(Integer idUsuario,Usuario usuario, String nuevaPassword);


}
