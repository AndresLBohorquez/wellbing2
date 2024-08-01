package com.devalb.wellbing2.entity;

import lombok.Data;

@Data
public class Email {
    private String destinatario;
    private String asunto;
    private String mensaje;
}
