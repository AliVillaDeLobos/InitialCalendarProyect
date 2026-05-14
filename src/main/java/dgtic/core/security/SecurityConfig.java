package dgtic.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
     @Autowired
     private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/css/**",
                                "/bootstrap/**",
                                "/tema/**",
                                "/images/**",
                                "/favicon.ico",
                                "/procesar-login",
                                "/login",
                                "/registrar"
                        ).permitAll()
                        .requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?invalid")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/procesar-login")
                        .usernameParameter("email")
                        .passwordParameter("usuarioPassword")
                        .defaultSuccessUrl("/auth/inicio", true)
//                        .successForwardUrl("/auth/inicio")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/acceso-denegado"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll())
                .csrf(Customizer.withDefaults())
                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14, new SecureRandom());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //Si no se hace asi se tiene que mandar en el SecurityFilterChain, donde esta comentado
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return new ProviderManager(authenticationProvider());
    }

}
