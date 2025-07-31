document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.signup-form');
    
    // 유효성 검사 패턴 정의
    const VALIDATION_PATTERNS = {
        LOGIN_ID: {
            pattern: /^[a-zA-Z][a-zA-Z0-9]{3,}$/,
            message: '아이디는 영문으로 시작하고, 영문, 숫자만 사용 가능하며 4자 이상이어야 합니다.'
        },
        PASSWORD: {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
            message: '비밀번호는 영문과 숫자를 포함하여 8자 이상이어야 합니다.'
        }
    };

    //동기적 커스텀한 알림창
    function customAlert(message){
        return new Promise((resolve) => {  // Promise로 감싸서 동기적 처리
            const alertDiv = document.createElement('div');
            alertDiv.className = 'custom-alert';
            alertDiv.innerHTML = `
                <div class="message">${message}</div>
                <button class="close-btn">확인</button>
            `;
            document.body.appendChild(alertDiv);
            alertDiv.style.display = 'block';

            alertDiv.querySelector('.close-btn').onclick = function() {
                alertDiv.remove();
                resolve();  // 확인 버튼 클릭 시에만 Promise 해결
            };
        });
    }

    // 성별 체크박스 중 하나만 선택되게 처리
    const genderCheckboxes = form.querySelectorAll('.gender-checkbox');
    genderCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                genderCheckboxes.forEach(cb => {
                    if (cb !== this) cb.checked = false;
                });
            }
        });
    });

    // 카카오 주소검색 API 연동
    document.querySelector('.zip-search-btn').addEventListener('click', function() {
        new daum.Postcode({
            oncomplete: function(data) {
                document.querySelector('input[name="memberZipCode"]').value = data.zonecode;
                document.querySelector('input[name="memberAddress1"]').value = data.roadAddress || data.jibunAddress;
            }
        }).open();
    });

    // 아이디 실시간 검증
    const loginIdInput = form.querySelector('input[name="memberLoginId"]');
    loginIdInput.addEventListener('input', function() {
        this.classList.remove('input-error');
        const errorMsg = this.closest('.input-group').querySelector('.error-message');
        if (errorMsg) errorMsg.remove();

        if (this.value && !VALIDATION_PATTERNS.LOGIN_ID.pattern.test(this.value)) {
            this.classList.add('input-error');
            const msg = document.createElement('div');
            msg.className = 'error-message';
            msg.innerText = VALIDATION_PATTERNS.LOGIN_ID.message;
            this.closest('.input-group').appendChild(msg);
        }
    });

    // 비밀번호 실시간 검증
    const passwordInput = form.querySelector('input[name="memberPassword"]');
    passwordInput.addEventListener('input', function() {
        this.classList.remove('input-error');
        const errorMsg = this.closest('.input-group').querySelector('.error-message');
        if (errorMsg) errorMsg.remove();

        if (this.value && !VALIDATION_PATTERNS.PASSWORD.pattern.test(this.value)) {
            this.classList.add('input-error');
            const msg = document.createElement('div');
            msg.className = 'error-message';
            msg.innerText = VALIDATION_PATTERNS.PASSWORD.message;
            this.closest('.input-group').appendChild(msg);
        }
    });

    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        let valid = true;

        // 모든 에러 메시지/에러 스타일 초기화
        form.querySelectorAll('.input-error').forEach(el => el.classList.remove('input-error'));
        form.querySelectorAll('.error-message').forEach(el => el.remove());

        // 아이디 검증
        if (!VALIDATION_PATTERNS.LOGIN_ID.pattern.test(loginIdInput.value)) {
            valid = false;
            loginIdInput.classList.add('input-error');
            const msg = document.createElement('div');
            msg.className = 'error-message';
            msg.innerText = VALIDATION_PATTERNS.LOGIN_ID.message;
            loginIdInput.closest('.input-group').appendChild(msg);
        }

        // 비밀번호 검증
        if (!VALIDATION_PATTERNS.PASSWORD.pattern.test(passwordInput.value)) {
            valid = false;
            passwordInput.classList.add('input-error');
            const msg = document.createElement('div');
            msg.className = 'error-message';
            msg.innerText = VALIDATION_PATTERNS.PASSWORD.message;
            passwordInput.closest('.input-group').appendChild(msg);
        }

        // 필수 입력값 검사
        const requiredFields = [
            { name: 'memberName', msg: '이름을 입력하세요.' },
            { name: 'memberBirthDate', msg: '생년월일을 입력하세요.' },
            { name: 'memberGender', msg: '성별을 선택하세요.' },
            { name: 'memberPhone', msg: '휴대폰 번호를 입력하세요.' },
            { name: 'memberEmail', msg: '이메일을 입력하세요.' },
            { name: 'memberAddress1', msg: '주소를 입력하세요.' }
        ];

        requiredFields.forEach(field => {
            const input = form.elements[field.name];
            if (!input || (input.type === 'checkbox' && !form.querySelector(`input[name="${field.name}"]:checked`))) {
                valid = false;
                const group = input ? input.closest('.input-group') : form.querySelector(`input[name="${field.name}"]`).closest('.input-group');
                if (group) {
                    if (input) input.classList.add('input-error');
                    const msg = document.createElement('div');
                    msg.className = 'error-message';
                    msg.innerText = field.msg;
                    group.appendChild(msg);
                }
            } else if (input.type !== 'checkbox' && input.value.trim() === '') {
                valid = false;
                input.classList.add('input-error');
                const group = input.closest('.input-group');
                const msg = document.createElement('div');
                msg.className = 'error-message';
                msg.innerText = field.msg;
                group.appendChild(msg);
            }
        });

        if (valid) {
            const formData = new FormData(form);
            // 체크박스 값 설정
            formData.set('memberEmailAgree', form.querySelector('input[name="memberEmailAgree"]').checked);
            formData.set('memberSmsAgree', form.querySelector('input[name="memberSmsAgree"]').checked);
            
            try {
                const response = await fetch(form.action, {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'Accept': 'text/html'
                    },
                    redirect: 'follow'  // 리다이렉트를 따라가도록 설정
                });
                
                // 리다이렉트된 경우
                if (response.redirected) {
                    customAlert('환영합니다!<br>회원가입에 성공하셨습니다.');
                    document.querySelector('.close-btn').onclick = function() {
                        window.location.href = response.url;
                    };
                    return;
                }
                
                const result = await response.text();
                console.log('서버 응답:', result); // 디버깅용
                
                // 응답이 HTML인 경우 (에러 발생)
                if (result.includes('<!DOCTYPE html>')) {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(result, 'text/html');
                    const errorDiv = doc.querySelector('.error-message');
                    
                    if (errorDiv) {
                        customAlert(errorDiv.textContent.trim());
                    }
                }
            } catch (error) {
                console.error('에러 발생:', error); // 디버깅용
                customAlert('회원가입 중 오류가 발생했습니다.');
            }
        }
    });

    // 입력 시 에러 표시 제거
    form.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', function() {
            this.classList.remove('input-error');
            const errorMsg = this.closest('.input-group').querySelector('.error-message');
            if (errorMsg) errorMsg.remove();
        });
    });
});