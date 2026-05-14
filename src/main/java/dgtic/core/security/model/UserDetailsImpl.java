package dgtic.core.security.model;

import dgtic.core.system.model.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


public class UserDetailsImpl  implements UserDetails {

    private Integer id;
    private String nombre;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {this.usuario = usuario;}

    public UserDetailsImpl(Integer id, String nombre, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getNombreRol())).collect(Collectors.toList());
        return new UserDetailsImpl(
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getUsuarioPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


}
