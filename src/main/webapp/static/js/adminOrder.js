function changePageSize(size) {
    window.location.href = '?page=0&size=' + size;
}

function changeStatus(orderId, orderStatus) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/admin/kmw/order/update/status';

    const orderIdInput = document.createElement('input');
    orderIdInput.type = 'hidden';
    orderIdInput.name = 'orderId';
    orderIdInput.value = orderId;

    const orderStatusInput = document.createElement('input');
    orderStatusInput.type = 'hidden';
    orderStatusInput.name = 'orderStatus';
    orderStatusInput.value = orderStatus;

    form.appendChild(orderIdInput);
    form.appendChild(orderStatusInput);
    document.body.appendChild(form);
    form.submit();
}

// 커스텀 알림창 함수 (확인/취소)
function examineCustomAlert(message) {
    return new Promise((resolve) => {
        const confirmDiv = document.createElement('div');
        confirmDiv.className = 'examine-custom-alert';
        confirmDiv.innerHTML = `
            <div class="message">${message}</div>
            <div class="button-group">
                <button class="close-btn">확인</button>
                <button class="close-btn">취소</button>
            </div>
        `;
        document.body.appendChild(confirmDiv);
        confirmDiv.style.display = 'block';

        const [okBtn, cancelBtn] = confirmDiv.querySelectorAll('.close-btn');
        okBtn.onclick = function() {
            confirmDiv.remove();
            resolve(true);
        };
        cancelBtn.onclick = function() {
            confirmDiv.remove();
            resolve(false);
        };
    });
}

// 단순 알림창 함수 (확인만)
function showCustomAlert(message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'examine-custom-alert';
    alertDiv.innerHTML = `
        <div class="message">${message}</div>
        <div class="button-group">
            <button class="close-btn">확인</button>
        </div>
    `;
    document.body.appendChild(alertDiv);
    alertDiv.style.display = 'block';

    const okBtn = alertDiv.querySelector('.close-btn');
    okBtn.onclick = function() {
        alertDiv.remove();
        window.location.reload();
    };
}

// 운송장번호 저장 함수
async function saveTrackingNumber(orderId) {
    const trackingNumber = document.getElementById('trackingNumber_' + orderId).value;
    
    const confirmed = await examineCustomAlert('운송장번호를 저장하시겠습니까?');
    if (confirmed) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/admin/kmw/order/tracking';
        
        const orderIdInput = document.createElement('input');
        orderIdInput.type = 'hidden';
        orderIdInput.name = 'orderId';
        orderIdInput.value = orderId;
        
        const trackingInput = document.createElement('input');
        trackingInput.type = 'hidden';
        trackingInput.name = 'trackingNumber';
        trackingInput.value = trackingNumber;
        
        form.appendChild(orderIdInput);
        form.appendChild(trackingInput);
        document.body.appendChild(form);
        
        try {
            const response = await fetch(form.action, {
                method: 'POST',
                body: new FormData(form)
            });
            
            if (response.ok) {
                showCustomAlert('운송장번호가 저장되었습니다.');
            } else {
                showCustomAlert('운송장번호 저장에 실패했습니다.');
            }
        } catch (error) {
            showCustomAlert('운송장번호 저장 중 오류가 발생했습니다.');
        }
    }
} 