const toggleBtn = document.querySelector('.navbar_toogleBtn');
const menu = document.querySelector('.navbar_menu');
const icon = document.querySelector('.navbar_icon');

if (toggleBtn && menu && icon) {
    toggleBtn.addEventListener('click', () => {
        menu.classList.toggle('active');
        icon.classList.toggle('active');
    });
} else {
    console.log("올바른 버튼을 연결하지 않았습니다.");
}
