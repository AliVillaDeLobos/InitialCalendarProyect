package dgtic.core.security.service;

import dgtic.core.security.model.UserDetailsImpl;
import dgtic.core.system.exceptions.ResourceNotFoundException;
import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("Usuario no encontrado"));
        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getNombreRol())).collect(Collectors.toList());
        return new UserDetailsImpl(
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getUsuarioPassword(), authorities);
    }

}
