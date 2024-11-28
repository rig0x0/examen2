package com.upiiz.examen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configuración de la seguridad personalizada
        return httpSecurity
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/api/v1/orders/**") // Ignorar CSRF en rutas
                        // específicas
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyAuthority("READ")
                            .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyAuthority("CREATE")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasAnyAuthority("UPDATE")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasAnyAuthority("DELETE")
                            .anyRequest().denyAll();
                })
                .build();
    }

    //authentication manager - Lo obtenemos de una instancia que ya existe
    public AuthenticationManager authenticationManager() throws Exception{
        return  authenticationConfiguration.getAuthenticationManager();
    }

    // authentication provider - DAO - Va a proporcionar la autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    // Password encoder
    public PasswordEncoder passwordEncoder(){
        //return  new BCryptPasswordEncoder();
        return  NoOpPasswordEncoder.getInstance();
    }

    // UserDetailsService - base de datos o usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService(){
        // Definir usuario en memoria por el momento
        UserDetails usuarioMiguel = User.withUsername("Miguel").password(passwordEncoder().encode("miguel1234")).roles("ADMIN").authorities(getAuthorities("ADMIN")).build();
        UserDetails usuarioRodrigo = User.withUsername("Rodrigo").password(passwordEncoder().encode("rodrigo1234")).roles("USER").authorities(getAuthorities("USER")).build();
        UserDetails usuarioFelipe = User.withUsername("Felipe").password(passwordEncoder().encode("felipe1234")).roles("MODERATOR").authorities(getAuthorities("MODERATOR")).build();
        UserDetails usuarioIsaac = User.withUsername("Isaac").password(passwordEncoder().encode("isaac1234")).roles("EDITOR").authorities(getAuthorities("EDITOR")).build();
        UserDetails usuarioCris = User.withUsername("Cris").password(passwordEncoder().encode("cris1234")).roles("DEVELOPER").authorities(getAuthorities("DEVELOPER")).build();
        UserDetails usuarioAlonso = User.withUsername("Alonso").password(passwordEncoder().encode("alonso1234")).roles("ANALYST").authorities(getAuthorities("ANALYST")).build();




        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(usuarioMiguel);
        userDetailsList.add(usuarioRodrigo);
        userDetailsList.add(usuarioFelipe);
        userDetailsList.add(usuarioIsaac);
        userDetailsList.add(usuarioCris);
        userDetailsList.add(usuarioAlonso);
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    private String[] getAuthorities(String role) {
        switch (role) {
            case "ADMIN":
                return new String[]{"READ", "CREATE", "UPDATE", "DELETE"};
            case "USER":
                return new String[]{"READ"};
            case "MODERATOR":
                return new String[]{"READ", "UPDATE"};
            case "EDITOR":
                return new String[]{"UPDATE"};
            case "DEVELOPER":
                return new String[]{"READ", "CREATE", "UPDATE", "DELETE", "CREATE-USER"};
            case "ANALYST":
                return new String[]{"READ", "DELETE"};
            default:
                return new String[]{};
        }
    }
}
