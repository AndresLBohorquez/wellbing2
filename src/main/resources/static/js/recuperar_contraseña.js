document.getElementById('recuperarContraseñaForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const correo = document.getElementById('correo').value;

    // Aquí puedes agregar la lógica para enviar el correo de recuperación de contraseña

    Swal.fire({
        title: "Enviado!",
        text: 'Se ha enviado la nueva contraseña a tu correo electrónico.',
        icon: "success"
    });
});
