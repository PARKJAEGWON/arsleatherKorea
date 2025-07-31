// 스크롤 시 헤더 스타일 변경
window.addEventListener('scroll', function() {
    const header = document.querySelector('.main-header');
    if (window.scrollY > 50) {
        header.style.background = 'rgba(255, 255, 255, 0.98)';
        header.style.boxShadow = '0 2px 5px rgba(0, 0, 0, 0.1)';
    } else {
        header.style.background = 'rgba(255, 255, 255, 0.98)';
        header.style.boxShadow = 'none';
    }
});

// 모바일 메뉴 토글 기능
document.addEventListener('DOMContentLoaded', function() {
    // 여기에 모바일 메뉴 토글 기능을 추가할 수 있습니다
});

// 이미지 지연 로딩
document.addEventListener('DOMContentLoaded', function() {
    const images = document.querySelectorAll('img[data-src]');
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.removeAttribute('data-src');
                observer.unobserve(img);
            }
        });
    });

    images.forEach(img => imageObserver.observe(img));
});

document.addEventListener('DOMContentLoaded', function() {
    new Swiper('.hero-swiper', {
        loop: true,
        autoplay: { delay: 3000 },
        pagination: { el: '.swiper-pagination', clickable: true },
        navigation: {
            nextEl: '.hero-section .swiper-button-next',
            prevEl: '.hero-section .swiper-button-prev'
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const recommendedSwipers = document.querySelectorAll('.recommended-swiper');
    recommendedSwipers.forEach((swiperEl, index) => {
        new Swiper(swiperEl, {
            slidesPerView: 4,
            spaceBetween: 20,
            navigation: {
                nextEl: swiperEl.querySelector('.swiper-button-next'),
                prevEl: swiperEl.querySelector('.swiper-button-prev')
            },
            breakpoints: {
                1024: { slidesPerView: 4 },
                768: { slidesPerView: 3 },
                480: { slidesPerView: 2 },
                0: { slidesPerView: 1 }
            }
        });
    });
});

// 이용약관 모달 제어
function openTermsModal() {
    const modal = document.getElementById('termsModal');
    if (modal) {
        modal.classList.add('show');
        document.body.style.overflow = 'hidden'; // 배경 스크롤 방지
    }
}

function closeTermsModal() {
    const modal = document.getElementById('termsModal');
    if (modal) {
        modal.classList.remove('show');
        document.body.style.overflow = ''; // 배경 스크롤 복원
    }
}

// 모달 외부 클릭 시 닫기
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('termsModal');
    if (modal) {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                closeTermsModal();
            }
        });
    }
}); 