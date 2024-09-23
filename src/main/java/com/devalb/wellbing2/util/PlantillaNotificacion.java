package com.devalb.wellbing2.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Carrito;
import com.devalb.wellbing2.entity.Orden;
import com.devalb.wellbing2.entity.Usuario;
import com.devalb.wellbing2.service.EmailService;

import jakarta.mail.MessagingException;

@Service
public class PlantillaNotificacion {

        @Autowired
        private EmailService emailService;

        public void enviarEmailDeConfirmacionOrden(Usuario usuario, Orden orden, List<Carrito> carritoElements)
                        throws MessagingException {
                String destinatario = usuario.getEmail();
                String asunto = "Confirmación de tu orden #" + orden.getId();

                // Crear el contenido del email con una plantilla HTML
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                                .append("<head>")
                                .append("<style>")
                                .append("body { font-family: Arial, sans-serif; }")
                                .append(".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; }")
                                .append(".header { background-color: #007bff; color: white; padding: 10px; text-align: center; }")
                                .append(".content { padding: 20px; background-color: #f9f9f9; }")
                                .append(".footer { text-align: center; padding: 10px; background-color: #007bff; color: white; }")
                                .append("</style>")
                                .append("</head>")
                                .append("<body>")
                                .append("<div class='container'>")
                                .append("<div class='header'>")
                                .append("<h2>¡Gracias por tu compra, ").append(usuario.getNombre()).append("!</h2>")
                                .append("</div>")
                                .append("<div class='content'>")
                                .append("<p>Hemos recibido tu orden con los siguientes detalles:</p>")
                                .append("<ul>")
                                .append("<li><strong>Nombre:</strong> ").append(orden.getNombre()).append("</li>")
                                .append("<li><strong>Dirección:</strong> ").append(orden.getDireccion()).append("</li>")
                                .append("<li><strong>Ciudad:</strong> ").append(orden.getCiudad()).append("</li>")
                                .append("<li><strong>Barrio:</strong> ").append(orden.getBarrio()).append("</li>")
                                .append("<li><strong>Total:</strong> $").append(String.format("%.2f", orden.getTotal()))
                                .append("</li>")
                                .append("</ul>")
                                .append("<h4>Productos</h4>")
                                .append("<table style='width: 100%; border-collapse: collapse;'>")
                                .append("<tr>")
                                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Producto</th>")
                                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Cantidad</th>")
                                .append("</tr>");

                // Añadir los productos del carrito a la tabla
                for (Carrito item : carritoElements) {
                        htmlContent.append("<tr>")
                                        .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                                        .append(item.getProducto().getNombre())
                                        .append("</td>")
                                        .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                                        .append(item.getCantidad())
                                        .append("</td>")
                                        .append("</tr>");
                }

                htmlContent.append("</table>")
                                .append("</div>")
                                .append("<div class='footer'>")
                                .append("<p>Gracias por comprar con nosotros. ¡Esperamos verte de nuevo!</p>")
                                .append("</div>")
                                .append("</div>")
                                .append("</body>")
                                .append("</html>");

                // Llamar al servicio de envío de emails
                emailService.sendHtmlEmail(destinatario, asunto, htmlContent.toString());
        }

        public void enviarEmailRecuperacionPassword(Usuario usuario, String nuevaPassword) throws MessagingException {
                String destinatario = usuario.getEmail();
                String asunto = "Recuperación de tu contraseña";

                // Crear el contenido del email con una plantilla HTML
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                                .append("<head>")
                                .append("<style>")
                                .append("body { font-family: Arial, sans-serif; }")
                                .append(".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; }")
                                .append(".header { background-color: #28a745; color: white; padding: 10px; text-align: center; }")
                                .append(".content { padding: 20px; background-color: #f9f9f9; }")
                                .append(".footer { text-align: center; padding: 10px; background-color: #28a745; color: white; }")
                                .append("</style>")
                                .append("</head>")
                                .append("<body>")
                                .append("<div class='container'>")
                                .append("<div class='header'>")
                                .append("<h2>Recuperación de contraseña</h2>")
                                .append("</div>")
                                .append("<div class='content'>")
                                .append("<p>Hola ").append(usuario.getNombre()).append(",</p>")
                                .append("<p>Has solicitado recuperar tu contraseña. Tu nueva contraseña temporal es:</p>")
                                .append("<p><strong>").append(nuevaPassword).append("</strong></p>")
                                .append("<p>Por favor, inicia sesión y asegúrate de cambiarla por una nueva en cuanto puedas.</p>")
                                .append("</div>")
                                .append("<div class='footer'>")
                                .append("<p>Gracias por usar nuestros servicios. Si no solicitaste este cambio, por favor, contacta con soporte.</p>")
                                .append("</div>")
                                .append("</div>")
                                .append("</body>")
                                .append("</html>");

                // Llamar al servicio de envío de emails con HTML
                emailService.sendHtmlEmail(destinatario, asunto, htmlContent.toString());
        }

}
