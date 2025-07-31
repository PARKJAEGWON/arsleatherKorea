// 이미지 변경 함수
function changeMainImage(src) {
    document.getElementById('mainImage').src = src;
    document.querySelectorAll('.thumbnail').forEach(thumb => {
        thumb.classList.remove('active');
        if(thumb.src === src) thumb.classList.add('active');
    });
}

// 수량 감소 함수
function decreaseQuantity() {
    const input = document.getElementById('quantity');
    if(input.value > 1) input.value = parseInt(input.value) - 1;
}

// 수량 증가 함수
function increaseQuantity() {
    const input = document.getElementById('quantity');
    const maxStock = parseInt(document.getElementById('quantity').getAttribute('max'));
    if(input.value < maxStock) input.value = parseInt(input.value) + 1;
}

// 장바구니 추가 함수
function addToCart() {
    const productId = document.getElementById('productId').value;
    const quantity = document.getElementById('quantity').value;

    // 폼 데이터 생성
    const formData = new FormData();
    formData.append('productId', productId);
    formData.append('quantity', quantity);

    // AJAX 요청으로 장바구니 추가
    fetch(`${pageContext}/cart/add`, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        } else {
            return response.text();
        }
    })
    .then(data => {
        if (data) {
            alert('장바구니에 추가되었습니다.');
            window.location.href = `${pageContext}/cart`;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('장바구니 추가 중 오류가 발생했습니다.');
    });
}

// 바로 구매 함수
function buyNow() {
    const productId = document.getElementById('productId').value;
    const quantity = document.getElementById('quantity').value;

    // 구매 페이지로 이동
    window.location.href = `/order/direct?productId=${productId}&quantity=${quantity}`;
}

// 탭 기능 구현
function showTab(tabName) {
    // 모든 탭 내용을 숨김
    const tabContents = document.querySelectorAll('.tab-content');
    tabContents.forEach(content => {
        content.style.display = 'none';
    });

    // 모든 탭 버튼의 active 클래스 제거
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(button => {
        button.classList.remove('active');
    });

    // 선택된 탭 내용을 보여주고 버튼을 활성화
    document.getElementById(`tab-${tabName}`).style.display = 'block';
    document.querySelector(`.tab-btn[onclick="showTab('${tabName}')"]`).classList.add('active');
}

// 페이지 로드 시 기본 탭 표시
document.addEventListener('DOMContentLoaded', () => {
    showTab('images');
}); 