$(document).ready(function () {
    $('form').submit(function (event) {
        var nombre = $('#name').val();
        var direccion = $('#address').val();
        var celular = $('#celular').val();
        var ciudad = $('#ciudad').val();
        var barrio = $('#barrio').val();

        if (nombre == '' || direccion == '' || celular == '' || ciudad == '' || barrio == '') {
            event.preventDefault();
            Swal.fire({
                title: 'Error',
                text: 'Por favor, complete todos los campos',
                icon: 'error',
                confirmButtonText: 'Aceptar'
            });
        }
    });
});