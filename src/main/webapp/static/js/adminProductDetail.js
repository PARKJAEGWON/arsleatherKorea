// 탭 전환 함수
function showTab(tab) {
    // 모든 탭 내용 숨기기
    document.getElementById('tab-images').style.display = 'none';
    document.getElementById('tab-desc').style.display = 'none';
    document.getElementById('tab-edit').style.display = 'none';
    
    // 모든 탭 버튼 비활성화
    var btns = document.querySelectorAll('.tab-btn');
    btns.forEach(function(btn) { 
        btn.classList.remove('active'); 
    });
    
    // 선택된 탭 내용 표시
    document.getElementById('tab-' + tab).style.display = 'block';
    
    // 선택된 탭 버튼 활성화
    if(tab === 'images') btns[0].classList.add('active');
    else if(tab === 'desc') btns[1].classList.add('active');
    else btns[2].classList.add('active');
}

// 폼 유효성 검사
function validateForm() {
    const form = document.getElementById('productUpdateForm');
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    // 모든 필수 입력 필드 검사
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('error');
            isValid = false;
        } else {
            input.classList.remove('error');
        }
    });
    
    // 에러 메시지 표시
    const errorDiv = document.getElementById('error-message');
    if (!isValid) {
        errorDiv.textContent = '모든 필수 항목을 입력해주세요.';
        errorDiv.style.display = 'block';
    } else {
        errorDiv.style.display = 'none';
    }
    
    return isValid;
}

// 페이지 로드 시 기본 탭 표시
document.addEventListener('DOMContentLoaded', function() {
    showTab('images');
    
    // 폼 제출 이벤트 리스너
    const form = document.getElementById('productUpdateForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
            }
        });
    }
}); 