// 토스페이먼츠 SDK 초기화
const tossPayments = TossPayments('test_ck_ZLKGPx4M3M1DlRakzbv43BaWypv1');

// 주소 검색
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('zipCode').value = data.zonecode;
            document.getElementById('address1').value = data.address;
            document.getElementById('address2').focus();
        }
    }).open();
}

// 주문자 정보 동일 처리
function toggleAddressByOrderer() {
    const checked = document.getElementById('sameAsOrderer').checked;
    const member = {
        name: memberName,
        phone: memberPhone,
        zip: memberZipCode,
        addr1: memberAddress1,
        addr2: memberAddress2
    };
    document.getElementById('receiverName').value = checked ? member.name : '';
    document.getElementById('receiverPhone').value = checked ? member.phone : '';
    document.getElementById('zipCode').value = checked ? member.zip : '';
    document.getElementById('address1').value = checked ? member.addr1 : '';
    document.getElementById('address2').value = checked ? member.addr2 : '';
    document.getElementById('receiverName').readOnly = checked;
    document.getElementById('receiverPhone').readOnly = checked;
    document.getElementById('zipCode').readOnly = checked;
    document.getElementById('address1').readOnly = checked;
    document.getElementById('address2').readOnly = checked;
}

// 무통장입금 정보 표시/숨김
function toggleBankInfo() {
    const bankRadio = document.getElementById('payBank');
    const bankInfoArea = document.getElementById('bankInfoArea');
    bankInfoArea.style.display = bankRadio.checked ? 'block' : 'none';
}

// 결제 처리
async function handlePayment() {
    // 1. 배송정보 검증
    if (!validateDeliveryInfo()) {
        alert('배송정보를 모두 입력해주세요.');
        return;
    }

    // 2. 약관 동의 확인
    if (!checkAgreements()) {
        alert('필수 약관에 동의해주세요.');
        return;
    }

    // 3. 결제 방식 확인
    const payMethod = document.querySelector('input[name="payMethod"]:checked').value;
    const form = document.querySelector('#orderForm');
    const formData = new FormData(form);
    const orderType = formData.get('orderType');

    // 4. 결제 처리
    if (payMethod === 'card') {
        const searchParams = new URLSearchParams();
        formData.forEach((value, key) => {
            searchParams.append(key, value);
        });

        try {
            await tossPayments.requestPayment('카드', {
                amount: getTotalAmount(),
                orderId: 'TEMP_' + new Date().getTime(),
                orderName: getOrderName(),
                successUrl: `${window.location.origin}/payments/success?${searchParams.toString()}`,
                failUrl: `${window.location.origin}/payments/fail`
            });
        } catch (error) {
            console.error('결제 요청 실패:', error);
            alert('결제 요청에 실패했습니다.');
        }
    } else {
        // 무통장입금 처리
        const depositor = document.querySelector('input[name="depositorName"]').value;
        if (!depositor) {
            alert('입금자명을 입력해주세요.');
            return;
        }

        form.method = 'post';
        form.action = orderType === 'cart' ? '/order/cart' : '/order/direct';
        form.submit();
    }
}

// 주문명 생성
function getOrderName() {
    const orderType = document.querySelector('input[name="orderType"]').value;
    const items = document.querySelectorAll('.order-item h3');
    
    if (items.length === 0) return '상품 주문';
    
    const firstItemName = items[0].textContent;
    if (items.length === 1) return firstItemName;
    
    return `${firstItemName} 외 ${items.length - 1}건`;
}

// 총 결제금액 계산
function getTotalAmount() {
    const totalElement = document.querySelector('.total b');
    return parseInt(totalElement.textContent.replace(/[^0-9]/g, ''));
}

// 배송정보 검증
function validateDeliveryInfo() {
    const required = [
        'receiverName',
        'receiverPhone',
        'zipCode',
        'address1',
        'address2'
    ];

    return required.every(id => {
        const value = document.getElementById(id).value;
        return value && value.trim() !== '';
    });
}

// 약관 동의 확인
function checkAgreements() {
    return document.getElementById('agree1').checked 
        && document.getElementById('agree2').checked;
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 전체 동의 체크박스
    document.getElementById('agreeAll').addEventListener('change', function() {
        document.getElementById('agree1').checked = this.checked;
        document.getElementById('agree2').checked = this.checked;
    });

    // 배송정보 초기화
    document.getElementById('sameAsOrderer').checked = false;
    toggleAddressByOrderer();

    // 결제 방법 이벤트 리스너
    document.getElementById('payBank').addEventListener('change', toggleBankInfo);
    document.getElementById('payCard').addEventListener('change', toggleBankInfo);
    
    // 초기 상태 설정
    toggleBankInfo();
});