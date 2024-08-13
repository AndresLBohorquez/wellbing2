package com.devalb.wellbing2.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Post {
    private Long id;
    private String titulo;
    private LocalDate fecha;
    private String autor;
    private String resumen;
}
