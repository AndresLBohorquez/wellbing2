function filterProducts() {
    const nameFilter = document.getElementById('filterName').value.toLowerCase();
    const categoryFilter = document.getElementById('filterCategory').value;
    const minPriceFilter = parseFloat(document.getElementById('filterMinPrice').value);
    const filterMinPriceOutput = document.getElementById('filterMinPriceOutput');

    const maxPriceFilter = parseFloat(document.getElementById('filterMaxPrice').value);
    const filterMaxPriceOutput = document.getElementById('filterMaxPriceOutput');

    filterMinPrice.addEventListener('input', () => {
        filterMinPriceOutput.value = filterMinPrice.value;
    });

    filterMaxPrice.addEventListener('input', () => {
        filterMaxPriceOutput.value = filterMaxPrice.value;
    });

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

document.getElementById('filterName').addEventListener('input', filterProducts);
document.getElementById('filterCategory').addEventListener('change', filterProducts);
document.getElementById('filterMinPrice').addEventListener('input', filterProducts);
document.getElementById('filterMaxPrice').addEventListener('input', filterProducts);
