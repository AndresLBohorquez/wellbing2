function checkScreenSize() {
    if (window.innerWidth < 768) { // 768px es un ejemplo, puedes ajustar según tus necesidades
        document.querySelector('.chart-container-ingresos').classList.remove('chart-ingresos');
    } else {
        document.querySelector('.chart-container-ingresos').classList.add('chart-ingresos'); // Opcional: Agrega la clase de nuevo en pantallas grandes
    }
}

// Verificar el tamaño de la pantalla cuando se carga la página
window.addEventListener('load', checkScreenSize);

// Verificar el tamaño de la pantalla cuando se redimensiona
window.addEventListener('resize', checkScreenSize);


// Opciones generales para todas las gráficas
var chartOptions = {
    scales: {
        y: {
            beginAtZero: true,
            grid: {
                color: 'rgba(0, 0, 0, 0.1)' // Color de las líneas horizontales
            }
        },
        x: {
            grid: {
                display: false // Opcional: Oculta las líneas verticales
            }
        }
    },
    plugins: {
        legend: {
            display: false // Oculta la leyenda
        }
    },
    responsive: true,
    maintainAspectRatio: false
};

