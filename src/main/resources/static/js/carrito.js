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
                        <input type="number" class="quantity-input mx-2" value="${producto.cantidad}" min="1">
                        <button class="quantity-btn quantity-btn-plus">+</button>
                    </div>
                    <div class="col-6 col-md-2 text-center">
                        <p class="item-price" data-price="${producto.precio}">$${producto.precio * producto.cantidad}</p>
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
    setupManualInputUpdate(); // Añadimos una nueva función para actualizar el input manualmente
}

// Actualizar el total del carrito
function updateCartTotal() {
    let total = 0;
    const prices = document.querySelectorAll('.item-price');
    prices.forEach(priceElement => {
        const price = parseFloat(priceElement.getAttribute('data-price'));
        const quantity = priceElement.closest('.cart-item').querySelector('.quantity-input').value;
        total += price * quantity;
    });
    document.getElementById('cartTotal').innerText = '$' + total.toFixed(2);
}

// Configurar los botones de cantidad
function setupQuantityButtons() {
    document.querySelectorAll('.quantity-btn-minus').forEach(button => {
        button.addEventListener('click', function () {
            const input = this.closest('.quantity-container').querySelector('.quantity-input');
            let value = parseInt(input.value);
            if (value > 1) {
                input.value = value - 1;
                actualizarCantidad(input);
            }
        });
    });

    document.querySelectorAll('.quantity-btn-plus').forEach(button => {
        button.addEventListener('click', function () {
            const input = this.closest('.quantity-container').querySelector('.quantity-input');
            let value = parseInt(input.value);
            input.value = value + 1;
            actualizarCantidad(input);
        });
    });
}

// Configurar los botones de eliminación
function setupDeleteButtons() {
    document.querySelectorAll('.btn-delete-product').forEach(button => {
        button.addEventListener('click', function () {
            const item = this.closest('.cart-item');
            const productId = item.getAttribute('data-id');
            eliminarProducto(productId);
        });
    });
}

// Nueva función: Actualizar cuando el input cambia manualmente
function setupManualInputUpdate() {
    document.querySelectorAll('.quantity-input').forEach(input => {
        // Detectar cuando el usuario presiona 'Enter' dentro del input
        input.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                actualizarCantidad(this);
            }
        });

        // Detectar cuando el input pierde el foco (blur)
        input.addEventListener('blur', function () {
            actualizarCantidad(this);
        });
    });
}

// Actualizar la cantidad en el backend y en la vista
function actualizarCantidad(input) {
    const cantidad = parseInt(input.value);
    const carritoId = input.closest('.cart-item').getAttribute('data-id');

    // Actualizar el precio en la vista
    const priceElement = input.closest('.cart-item').querySelector('.item-price');
    const pricePerUnit = parseFloat(priceElement.getAttribute('data-price'));
    priceElement.innerText = '$' + (pricePerUnit * cantidad).toFixed(2);

    // Llamada al backend para actualizar la cantidad en el servidor sin la acción
    fetch('/carrito/actualizarCantidad', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            carritoId: carritoId,
            cantidad: cantidad
        })
    }).then(response => {
        if (response.ok) {
            // Si la respuesta es correcta, actualizamos el total del carrito
            updateCartTotal();
        } else {
            console.error('Error al actualizar la cantidad');
        }
    }).catch(error => console.error('Error:', error));
}

// Cargar el carrito al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    actualizarVistaCarrito();
});
