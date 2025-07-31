document.addEventListener('DOMContentLoaded', function() {
    // pageContext를 전역 변수로 선언
    const pageContext = document.querySelector('meta[name="context-path"]')?.getAttribute('content') || '';

    // 수량 조절 버튼 이벤트 리스너
    document.querySelectorAll('.quantity-btn').forEach(button => {
        button.addEventListener('click', function() {
            const input = this.parentElement.querySelector('.quantity-input');
            let quantity = parseInt(input.value);
            
            if (this.dataset.action === 'increase') {
                quantity++;
            } else if (this.dataset.action === 'decrease') {
                quantity = Math.max(1, quantity - 1);
            }
            
            input.value = quantity;
            updateTotalPrice();
            // 수량 변경 시 서버로 요청
            updateCartQuantity(this.dataset.cartId, quantity);
        });
    });

    // 수량 입력 필드 이벤트 리스너
    document.querySelectorAll('.quantity-input').forEach(input => {
        input.addEventListener('change', function() {
            let quantity = parseInt(this.value);
            if (isNaN(quantity) || quantity < 1) {
                this.value = 1;
                alert('수량은 1개 이상이어야 합니다.');
            }
            updateTotalPrice();
            // 수량 변경 시 서버로 요청
            updateCartQuantity(this.dataset.cartId, quantity);
        });
    });

    // 삭제 버튼 이벤트 리스너
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const cartId = this.dataset.cartId;
            showDeleteConfirm(cartId);
        });
    });
});

// 총 가격 업데이트 함수
function updateTotalPrice() {
    let total = 0;
    document.querySelectorAll('.cart-items tbody tr').forEach(row => {
        const price = parseInt(row.querySelector('td:nth-child(3)').textContent.replace(/[^0-9]/g, ''));
        const quantity = parseInt(row.querySelector('.quantity-input').value);
        total += price * quantity;
    });
    
    // 총 가격 표시 업데이트
    document.querySelector('.total-price').textContent = 
        `총 주문금액: ${total.toLocaleString()}원`;
}

// 수량 업데이트 함수
function updateCartQuantity(cartId, quantity) {
    const pageContext = document.querySelector('meta[name="context-path"]')?.getAttribute('content') || '';
    
    // form을 동적으로 생성하여 POST 요청
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${pageContext}/cart/update`;
    
    const cartIdInput = document.createElement('input');
    cartIdInput.type = 'hidden';
    cartIdInput.name = 'cartId';
    cartIdInput.value = cartId;
    
    const quantityInput = document.createElement('input');
    quantityInput.type = 'hidden';
    quantityInput.name = 'quantity';
    quantityInput.value = quantity;
    
    form.appendChild(cartIdInput);
    form.appendChild(quantityInput);
    document.body.appendChild(form);
    form.submit();
}

// 주문하기 버튼 클릭 시
document.querySelector('.checkout-btn')?.addEventListener('click', function(e) {
    e.preventDefault();
    
    const cartItems = [];
    document.querySelectorAll('.cart-items tbody tr').forEach(row => {
        cartItems.push({
            cartId: row.querySelector('.quantity-btn').dataset.cartId,
            quantity: parseInt(row.querySelector('.quantity-input').value)
        });
    });
    
    // 주문 페이지로 이동하면서 수량 정보 전달
    const pageContext = document.querySelector('meta[name="context-path"]')?.getAttribute('content') || '';
    const checkoutUrl = `${pageContext}/order/cart`;
    
    // 수량 정보를 세션스토리지에 저장
    sessionStorage.setItem('cartQuantities', JSON.stringify(cartItems));
    
    // 주문 페이지로 이동
    window.location.href = checkoutUrl;
});

// 삭제 확인 알림창 표시
function showDeleteConfirm(cartId) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'examine-custom-alert';
    alertDiv.innerHTML = `
        <div class="message">정말 삭제하시겠습니까?</div>
        <div class="button-group">
            <button class="close-btn" onclick="deleteItem(${cartId})">확인</button>
            <button class="close-btn" onclick="closeAlert()">취소</button>
        </div>
    `;
    document.body.appendChild(alertDiv);
    alertDiv.style.display = 'block';
}

// 알림창 닫기
function closeAlert() {
    const alert = document.querySelector('.examine-custom-alert');
    if (alert) {
        alert.remove();
    }
}

// 삭제 실행
function deleteItem(cartId) {
    closeAlert();
    
    const pageContext = document.querySelector('meta[name="context-path"]')?.getAttribute('content') || '';
    
    // form을 동적으로 생성하여 POST 요청
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${pageContext}/cart/delete`;
    
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'cartId';
    input.value = cartId;
    
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
} 