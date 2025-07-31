// 커스텀 confirm 함수
function showConfirm(message, onConfirm) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'examine-custom-alert';
    alertDiv.innerHTML = `
        <div class="message">${message}</div>
        <div class="button-group">
            <button class="close-btn confirm-btn">확인</button>
            <button class="close-btn cancel-btn" style="background:#666;">취소</button>
        </div>
    `;
    document.body.appendChild(alertDiv);
    alertDiv.style.display = 'block';

    // 확인 버튼 클릭
    alertDiv.querySelector('.confirm-btn').addEventListener('click', () => {
        document.body.removeChild(alertDiv);
        onConfirm();
    });

    // 취소 버튼 클릭
    alertDiv.querySelector('.cancel-btn').addEventListener('click', () => {
        document.body.removeChild(alertDiv);
    });
}
// 커스텀 alert 함수
function showAlert(message, reload = false) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'custom-alert';
    alertDiv.innerHTML = `
        <div class="message">${message}</div>
        <button class="close-btn">확인</button>
    `;
    document.body.appendChild(alertDiv);
    alertDiv.style.display = 'block';

    alertDiv.querySelector('.close-btn').addEventListener('click', () => {
        document.body.removeChild(alertDiv);
        if(reload) location.reload();
    });
}

// 주문 취소 처리
function cancelOrder(orderId) {
    showConfirm('주문을 취소하시겠습니까?', () => {
        fetch('/order/' + orderId + '/cancel', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if(response.ok) {
                showAlert('주문이 취소되었습니다.', true);
            } else {
                showAlert('주문 취소에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('주문 취소 중 오류가 발생했습니다.');
        });
    });
}

// 반품 신청 처리
function returnOrder(orderId) {
    showConfirm('반품을 신청하시겠습니까?', () => {
        fetch('/order/' + orderId + '/return', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if(response.ok) {
                showAlert('반품 신청이 완료되었습니다.', true);
            } else {
                showAlert('반품 신청에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('반품 신청 중 오류가 발생했습니다.');
        });
    });
}