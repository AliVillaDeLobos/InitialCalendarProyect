package dgtic.core.system.service;

import dgtic.core.system.dto.UsuarioDto;
import dgtic.core.system.exceptions.PasswordInvalidaException;
import dgtic.core.system.mapper.UsuarioMapper;
import dgtic.core.system.model.entities.Rol;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.repository.RolRepository;
import dgtic.core.system.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper mapper, RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        Rol rol = rolRepository.findByNombreRol("USER").orElseThrow(
                () -> new ResolutionException("Rol no encontrado"));
        usuario.setRoles(List.of(rol));
        usuario.setUsuarioPassword(passwordEncoder.encode(usuario.getUsuarioPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario update(Integer idUsuario, Usuario usuario, String nuevaPassword) {
        Usuario usrExistente = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new EntityNotFoundException("Usuario no encontrado"));
        String passwordActual = usuario.getUsuarioPassword();

        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty() && !usuario.getEmail().equals(usrExistente.getEmail())) {
            boolean emailExiste = usuarioRepository.findByEmail(usuario.getEmail())
                    .filter(u -> !u.getIdUsuario().equals(idUsuario))
                    .isPresent();
            if (emailExiste) {
                throw new IllegalArgumentException("El email ya está registrado. Intenta otro");
            }
            usrExistente.setEmail(usuario.getEmail());
        }

        if(nuevaPassword != null && !nuevaPassword.isEmpty() ) {
            if (passwordActual == null || passwordActual.isEmpty()) {
                throw new PasswordInvalidaException("Debes ingresar tu contraseña actual para cambiarla");
            }
            if (!passwordEncoder.matches(passwordActual, usrExistente.getUsuarioPassword())) {
                throw new PasswordInvalidaException("La contraseña actual no es correcta");
            }

            if (passwordEncoder.matches(nuevaPassword, usrExistente.getUsuarioPassword())) {
                throw new PasswordInvalidaException("La nueva contraseña no puede ser igual a la anterior");
            }
            usrExistente.setUsuarioPassword(passwordEncoder.encode(nuevaPassword));
        }

        if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
            usrExistente.setNombre(usuario.getNombre());
        }
        if(usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isEmpty()) {
            usrExistente.setApellidoPaterno(usuario.getApellidoPaterno());
        }
        if(usuario.getApellidoMaterno() != null && !usuario.getApellidoMaterno().isEmpty()) {
            usrExistente.setApellidoMaterno(usuario.getApellidoMaterno());
        }
        return usuarioRepository.save(usrExistente);
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return Optional.ofNullable(usuarioRepository.findById(id).orElse(null));
    }


    public Usuario obtenerUsuarioDesdeDto(UsuarioDto dto) {
        if (dto == null || dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("DTO inválido");
        }
        return usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}
