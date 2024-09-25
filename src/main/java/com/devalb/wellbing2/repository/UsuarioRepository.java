package com.devalb.wellbing2.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devalb.wellbing2.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);

    public Usuario findByEmail(String email);

    public Usuario findByCodigoUsuario(String codigoUsuario);

    @Query(value = "SELECT * FROM usuario WHERE visible = true", nativeQuery = true)
    public List<Usuario> findAllVisible();

    public int countByFechaRegistroBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
