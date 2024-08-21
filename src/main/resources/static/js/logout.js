function logout() {
    // Creamos un formulario temporal
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '/logout'); // Ajusta la ruta según tu configuración

    // Creamos un campo oculto para la protección CSRF si es necesario (consulta la documentación de Spring Security)
    var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    var csrfParameter = document.querySelector('meta[name="_csrf_parameter"]').getAttribute('content');
    var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    var csrfInput = document.createElement('input');
    csrfInput.setAttribute('type', 'hidden');
    csrfInput.setAttribute('name', csrfParameter);
    csrfInput.setAttribute('value', csrfToken);
    form.appendChild(csrfInput);

    // Añadimos el formulario al DOM y lo enviamos
    document.body.appendChild(form);
    form.submit();
}