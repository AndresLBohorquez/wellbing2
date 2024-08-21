const toggleSidebar = document.querySelector('.toggle-sidebar');
const sidebar = document.querySelector('.sidebar');

toggleSidebar.addEventListener('click', () => {
  sidebar.classList.toggle('show');
  toggleSidebar.classList.toggle('show');
});