package com.devalb.wellbing2.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Rol;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.repository.UsuarioRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario us = usuarioRepository.findByUsername(username);

            if (us == null) {
                us = usuarioRepository.findByEmail(username);
            }

            if (us == null || !us.getEstadoUsuario().getNombre().equals("Activo")) {
                throw new UsernameNotFoundException("Usuario no encontrado o no está activo");
            }

            List<GrantedAuthority> roles = new ArrayList<>();
            for (Rol rol : us.getRoles()) {
                roles.add(new SimpleGrantedAuthority(rol.getNombre()));
            }

            return new User(us.getUsername(), us.getPassword(), roles);

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error al cargar el usuario", e);
        }
    }
}