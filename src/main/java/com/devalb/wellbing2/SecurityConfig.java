package com.devalb.wellbing2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.devalb.wellbing2.service.Impl.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        String[] resources = new String[] {
                        "/include/**", "/css/**", "/icons/**", "/images/**", "/js/**", "/templates/layout/**",
                        "/webjars/**", "/error/**", "/prueba", "/productos/**"
        };

        @Autowired
        private UserService userService;

        private BCryptPasswordEncoder bcrypt;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
                return bCryptPasswordEncoder;
        }

        public void configure(AuthenticationManagerBuilder auth)
                        throws Exception {
                auth.userDetailsService(userService).passwordEncoder(bcrypt);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(resources).permitAll()
                                                .requestMatchers("/", "/signup", "/productos", "/login", "/nosotros",
                                                                "/email**", "/recuperar-pass", "/producto_detalle/**",
                                                                "/politicas_privacidad", "/aviso_legal",
                                                                "/terminos_condiciones", "favicon.ico")
                                                .permitAll()
                                                
                                                .requestMatchers("/admin", "/admin/pago-mensual/**")
                                                .hasAuthority("Admin")
                                                .requestMatchers("/admin/usuarios/**", "/admin/email/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Tesorero", "Domiciliario")
                                                .requestMatchers("/admin/usuarios/eliminar/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Tesorero", "Domiciliario")
                                                .requestMatchers("/admin/categorias/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Domiciliario")
                                                .requestMatchers("/admin/productos/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Domiciliario")
                                                .requestMatchers("/admin/activaciones/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Tesorero")
                                                .requestMatchers("/admin/equipo/**")
                                                .hasAnyAuthority("Admin", "Secretario", "Tesorero")
                                                .requestMatchers("/admin/ordenes/**", "/admin/ordenes-editar/**")
                                                .hasAnyAuthority("Admin", "Domiciliario")
                                                .requestMatchers("/admin/pagos/**").hasAnyAuthority("Admin", "Tesorero")
                                                .requestMatchers("/admin/pqrs/**")
                                                .hasAnyAuthority("Admin", "Secretario")
                                                .requestMatchers("/admin/wellpoints/**").hasAuthority("Admin")
                                                .requestMatchers("/secretario/**").hasAuthority("Secretario")
                                                .requestMatchers("/tesorero/**").hasAuthority("Tesorero")
                                                .requestMatchers("/usuario/**", "/carrito/**", "/checkout/**")
                                                .hasAuthority("Usuario")
                                                .anyRequest().denyAll()

                                // .requestMatchers("/**").hasRole("USER")
                                // .requestMatchers("/login?OK").permitAll()

                                )

                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .permitAll()
                                                .defaultSuccessUrl("/")
                                                .failureUrl("/login?error=true")
                                                .usernameParameter("username")
                                                .passwordParameter("password"))
                                .logout(logout -> logout
                                                .permitAll()
                                                .logoutSuccessUrl("/login?logout"));

                return http.build();
        }

}