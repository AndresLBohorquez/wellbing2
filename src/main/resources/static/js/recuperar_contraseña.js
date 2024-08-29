document.getElementById('recuperarContraseñaForm').addEventListener('submit', function (event) {
    //event.preventDefault();
    const correo = document.getElementById('correo').value;

    fetch('/recuperar-pass', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'destinatario': correo
        })
    })
    .then(response => response.text())
    .then(data => {
        Swal.fire({
            title: "Enviado!",
            text: 'Se ha enviado la nueva contraseña a tu correo electrónico.',
            icon: "success"
        });
    })
    .catch(error => {
        Swal.fire({
            title: "Error!",
            text: 'Hubo un problema al enviar la nueva contraseña.',
            icon: "error"
        });
    });
});
