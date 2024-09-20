// Filtro de productos por nombre, categoría y rango de precios
function filterProducts() {
    const nameFilter = document.getElementById('filterName').value.toLowerCase();
    const categoryFilter = document.getElementById('filterCategory').value;
    const minPriceFilter = parseFloat(document.getElementById('filterMinPrice').value);
    const maxPriceFilter = parseFloat(document.getElementById('filterMaxPrice').value);

    const products = document.querySelectorAll('.product');

    products.forEach(product => {
        const productName = product.getAttribute('data-name').toLowerCase();
        const productCategory = product.getAttribute('data-category');
        const productPrice = parseFloat(product.getAttribute('data-price'));

        const matchesName = productName.includes(nameFilter);
        const matchesCategory = categoryFilter ? productCategory === categoryFilter : true;
        const matchesMinPrice = !isNaN(minPriceFilter) ? productPrice >= minPriceFilter : true;
        const matchesMaxPrice = !isNaN(maxPriceFilter) ? productPrice <= maxPriceFilter : true;

        if (matchesName && matchesCategory && matchesMinPrice && matchesMaxPrice) {
            product.style.display = 'block';
        } else {
            product.style.display = 'none';
        }
    });
}

// Sincronización de los campos range con los campos de entrada manual
document.addEventListener('DOMContentLoaded', function () {
    const filterMinPrice = document.getElementById('filterMinPrice');
    const filterMinPriceInput = document.getElementById('filterMinPriceInput');
    const filterMaxPrice = document.getElementById('filterMaxPrice');
    const filterMaxPriceInput = document.getElementById('filterMaxPriceInput');

    filterMinPrice.addEventListener('input', function () {
        filterMinPriceInput.value = filterMinPrice.value;
        filterProducts();
    });
    filterMinPriceInput.addEventListener('input', function () {
        filterMinPrice.value = filterMinPriceInput.value;
        filterProducts();
    });

    filterMaxPrice.addEventListener('input', function () {
        filterMaxPriceInput.value = filterMaxPrice.value;
        filterProducts();
    });
    filterMaxPriceInput.addEventListener('input', function () {
        filterMaxPrice.value = filterMaxPriceInput.value;
        filterProducts();
    });
});

// Filtros de nombre y categoría
document.getElementById('filterName').addEventListener('input', filterProducts);
document.getElementById('filterCategory').addEventListener('change', filterProducts);