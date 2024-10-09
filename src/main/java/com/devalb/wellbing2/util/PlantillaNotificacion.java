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

        public void enviarEmailInactividad(Usuario usuario) throws MessagingException {
                String destinatario = usuario.getEmail();
                String asunto = "Notificación de Inactividad";

                // Crear el contenido del email con una plantilla HTML
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                                .append("<head>")
                                .append("<style>")
                                .append("body { font-family: Arial, sans-serif; }")
                                .append(".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; }")
                                .append(".header { background-color: #dc3545; color: white; padding: 10px; text-align: center; }")
                                .append(".content { padding: 20px; background-color: #f9f9f9; }")
                                .append(".footer { text-align: center; padding: 10px; background-color: #dc3545; color: white; }")
                                .append("</style>")
                                .append("</head>")
                                .append("<body>")
                                .append("<div class='container'>")
                                .append("<div class='header'>")
                                .append("<h2>Notificación de Inactividad</h2>")
                                .append("</div>")
                                .append("<div class='content'>")
                                .append("<p>Hola ").append(usuario.getNombre()).append(",</p>")
                                .append("<p>Hemos notado que llevas ").append(usuario.getInactivo())
                                .append(" mes(es) sin actividad en nuestra plataforma. Recuerda que luego de 3 meses de inactividad tu cuenta será baneada y perderás tu progreso con nosotros</p>")
                                .append("<p>Queremos recordarte que puedes acceder a tu cuenta y continuar disfrutando de nuestros servicios en cualquier momento.</p>")
                                .append("<p>Si necesitas asistencia, no dudes en ponerte en contacto con nosotros.</p>")
                                .append("</div>")
                                .append("<div class='footer'>")
                                .append("<p>Gracias por confiar en nosotros. Esperamos verte pronto.</p>")
                                .append("<p>El equipo de soporte de WellBing</p>")
                                .append("</div>")
                                .append("</div>")
                                .append("</body>")
                                .append("</html>");

                // Llamar al servicio de envío de emails con HTML
                emailService.sendHtmlEmail(destinatario, asunto, htmlContent.toString());
        }

        public void enviarEmailBaneo(Usuario usuario) throws MessagingException {
                String destinatario = usuario.getEmail();
                String asunto = "Notificación de Baneo de Cuenta";

                // Crear el contenido del email con una plantilla HTML
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                                .append("<head>")
                                .append("<style>")
                                .append("body { font-family: Arial, sans-serif; }")
                                .append(".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; }")
                                .append(".header { background-color: #dc3545; color: white; padding: 10px; text-align: center; }")
                                .append(".content { padding: 20px; background-color: #f9f9f9; }")
                                .append(".footer { text-align: center; padding: 10px; background-color: #dc3545; color: white; }")
                                .append("</style>")
                                .append("</head>")
                                .append("<body>")
                                .append("<div class='container'>")
                                .append("<div class='header'>")
                                .append("<h2>Notificación de Baneo</h2>")
                                .append("</div>")
                                .append("<div class='content'>")
                                .append("<p>Hola ").append(usuario.getNombre()).append(",</p>")
                                .append("<p>Lamentamos informarte que tu cuenta ha sido suspendida debido a una violación de nuestras políticas y términos de uso.</p>")
                                .append("<p>Esta decisión se ha tomado tras una revisión detallada de tu actividad en la plataforma.</p>")
                                .append("<p>Si crees que esto es un error o deseas apelar la decisión, por favor contacta con nuestro equipo de soporte.</p>")
                                .append("</div>")
                                .append("<div class='footer'>")
                                .append("<p>Gracias por tu comprensión.</p>")
                                .append("<p>El equipo de soporte de WellBing</p>")
                                .append("</div>")
                                .append("</div>")
                                .append("</body>")
                                .append("</html>");

                // Llamar al servicio de envío de emails con HTML
                emailService.sendHtmlEmail(destinatario, asunto, htmlContent.toString());
        }

        public void enviarEmailReporteMensual(Usuario usuario, double cantidadWellPoints) throws MessagingException {
                String destinatario = usuario.getEmail();
                String asunto = "Reporte Mensual y Ganancia de WellPoints";

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
                                .append("<h2>Reporte Mensual - Ganancia de WellPoints</h2>")
                                .append("</div>")
                                .append("<div class='content'>")
                                .append("<p>Hola ").append(usuario.getNombre()).append(",</p>")
                                .append("<p>¡Nos complace informarte que se ha generado tu reporte mensual!</p>")
                                .append("<p>En este periodo, has ganado un total de <strong>")
                                .append(cantidadWellPoints).append(" WellPoints</strong>.</p>")
                                .append("<p>Continúa participando en nuestras actividades y acumulando WellPoints para canjear increíbles recompensas.</p>")
                                .append("<p>Pronto nos pondremos en contacto contigo.</p>")
                                .append("<p>Te agradecemos por ser parte de la comunidad WellBing. Si tienes alguna duda o necesitas asistencia, no dudes en ponerte en contacto con nuestro equipo de soporte.</p>")
                                .append("</div>")
                                .append("<div class='footer'>")
                                .append("<p>Gracias por tu confianza.</p>")
                                .append("<p>El equipo de soporte de WellBing</p>")
                                .append("</div>")
                                .append("</div>")
                                .append("</body>")
                                .append("</html>");

                // Llamar al servicio de envío de emails con HTML
                emailService.sendHtmlEmail(destinatario, asunto, htmlContent.toString());
        }

}
