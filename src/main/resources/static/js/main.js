// Función para actualizar la altura del nav y la posición del sidebar y botón toggle
function updateNavHeight() {
    const nav = document.querySelector('nav');
    const navHeight = nav.offsetHeight;

    // Actualiza la variable CSS
    document.documentElement.style.setProperty('--nav-height', `${navHeight}px`);

    // Actualiza la altura del sidebar en pantallas grandes
    const footer = document.querySelector('footer');
    const sidebar = document.querySelector('#sidebar');
    const footerOffset = footer.offsetTop;
    const sidebarHeight = footerOffset - navHeight;

    sidebar.style.height = `${sidebarHeight}px`;

    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar1 = document.getElementById('sidebar');

    sidebarToggle.addEventListener('click', () => {
        sidebar1.classList.toggle('nav-open');
        sidebarToggle.classList.toggle('nav-open');
    });
}

// Llama a la función al cargar la página y al redimensionar la ventana
window.addEventListener('load', updateNavHeight);
window.addEventListener('resize', updateNavHeight);
