// Función para actualizar el carrito en la vista
function actualizarVistaCarrito() {
    const carrito = obtenerCarritoUsuario();
    const carritoContenedor = document.getElementById('cartItemsContainer');

    carritoContenedor.innerHTML = '';

    carrito.forEach(producto => {
        const itemHTML = `
            <div class="cart-item mb-4" data-id="${producto.id}">
                <div class="row align-items-center">
                    <div class="col-3 col-md-2">
                        <img src="${producto.imagen}" alt="${producto.nombre}" class="img-fluid">
                    </div>
                    <div class="col-9 col-md-5">
                        <p class="item-name mb-1">${producto.nombre}</p>
                        <p class="text-muted mb-0">${producto.categoria}</p>
                    </div>
                    <div class="col-6 col-md-2 d-flex align-items-center justify-content-center quantity-container">
                        <button class="quantity-btn quantity-btn-minus">-</button>
                        <input type="number" class="quantity-input mx-2" value="${producto.cantidad}" min="1" readonly>
                        <button class="quantity-btn quantity-btn-plus">+</button>
                    </div>
                    <div class="col-6 col-md-2 text-center">
                        <p class="item-price" data-price="${producto.precio}">$${producto.precio}</p>
                    </div>
                    <div class="col-12 col-md-1 text-end text-md-center mt-2 mt-md-0">
                        <button class="btn-delete-product">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
        carritoContenedor.insertAdjacentHTML('beforeend', itemHTML);
    });

    updateCartTotal();
    setupQuantityButtons();
    setupDeleteButtons();
}

// Cargar el carrito al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    actualizarVistaCarrito();
});
