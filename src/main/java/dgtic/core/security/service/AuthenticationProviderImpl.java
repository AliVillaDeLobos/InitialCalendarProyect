package dgtic.core.security.service;

import dgtic.core.system.model.entities.Usuario;
import dgtic.core.system.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(
                () -> new BadCredentialsException("Usuario no encontrado"));
        if (passwordEncoder.matches(password, usuario.getUsuarioPassword())) {
            List<GrantedAuthority> autorities = usuario.getRoles().stream().map(role -> new SimpleGrantedAuthority(
                    role.getNombreRol())).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, password, autorities);
        } else {
            throw new BadCredentialsException("Usuario no encontrado");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
