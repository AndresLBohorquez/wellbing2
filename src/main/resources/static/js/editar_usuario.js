document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formEditarUsuario");

    form.addEventListener("submit", function (event) {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        if (username.length < 7) {
            Swal.fire({
                title: "Error!",
                text: 'El nombre de usuario debe tener al menos 7 caracteres.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }

        if (password.length > 0 && password.length < 6) {
            Swal.fire({
                title: "Error!",
                text: 'La contraseña debe tener al menos 6 caracteres si decides cambiarla.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }

        return true;
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const formRedSocial = document.getElementById("formRedSocial");

    formRedSocial.addEventListener("submit", function (event) {
        const link = document.getElementById("link").value;

        if (link.trim() === "") {
            Swal.fire({
                title: "Error!",
                text: 'El enlace no puede estar vacío.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }
        return true;
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const formMedioPago = document.getElementById("formMedioPago");

    formMedioPago.addEventListener("submit", function (event) {
        const nombre = document.getElementById("nombre").value;
        const numero = document.getElementById("numero").value;

        if (nombre.trim() === "" || numero.trim() === "") {
            Swal.fire({
                title: "Error!",
                text: 'Todos los campos son obligatorios.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }

        // Aquí puedes agregar más validaciones si es necesario

        return true;
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const formActivacion = document.getElementById("formActivacion");
    const comprobanteInput = document.getElementById("comprobante");

    formActivacion.addEventListener("submit", function (event) {
        const valor = document.getElementById("valor").value;

        if (valor <= 0) {
            Swal.fire({
                title: "Error!",
                text: 'El valor debe ser positivo.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }

        if (comprobanteInput.files.length > 0 && comprobanteInput.files[0].size > 2 * 1024 * 1024) {
            Swal.fire({
                title: "Error!",
                text: 'El archivo de comprobante debe ser menor a 2 MB.',
                icon: "error"
            });
            event.preventDefault();
            return false;
        }

        return true;
    });
});
