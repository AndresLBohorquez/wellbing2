document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const password = document.getElementById('password').value;
    const username = document.getElementById('username').value;
    const celular = document.getElementById('celular').value;

    if (password.length < 6) {
        Swal.fire({
            title: "Error!",
            text: 'La contraseña debe tener al menos 6 caracteres.',
            icon: "error"
        });
        return;
    }
    if (username.length < 6) {
        Swal.fire({
            title: "Error!",
            text: 'El nombre de usuario debe tener al menos 7 caracteres.',
            icon: "error"
        });
        return;
    }
    if (celular.length !== 10 || isNaN(celular)) {
        Swal.fire({
            title: "Error!",
            text: 'El número de celular debe tener exactamente 10 caracteres y ser numérico.',
            icon: "error"
        });
        return;
    }

    // Si todo está bien, enviar el formulario
    this.submit();
});