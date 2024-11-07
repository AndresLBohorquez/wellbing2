document
  .getElementById("registerForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmar-password").value; // Campo de confirmar contraseña
    const username = document.getElementById("username").value;
    const celular = document.getElementById("celular").value;

    // Validación de longitud de contraseña
    if (password.length < 6) {
      Swal.fire({
        title: "Error!",
        text: "La contraseña debe tener al menos 6 caracteres.",
        icon: "error",
      });
      return;
    }

    // Validación de coincidencia de contraseñas
    if (password !== confirmPassword) {
      Swal.fire({
        title: "Error!",
        text: "Las contraseñas no coinciden.",
        icon: "error",
      });
      return;
    }

    // Validación de longitud de nombre de usuario
    if (username.length < 7) {
      Swal.fire({
        title: "Error!",
        text: "El nombre de usuario debe tener al menos 7 caracteres.",
        icon: "error",
      });
      return;
    }

    // Validación del número de celular
    if (celular.length !== 10 || isNaN(celular)) {
      Swal.fire({
        title: "Error!",
        text: "El número de celular debe tener exactamente 10 caracteres y ser numérico.",
        icon: "error",
      });
      return;
    }

    // Si todas las validaciones son correctas, enviar el formulario
    this.submit();
  });
