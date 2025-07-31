document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('form');

    //비동기적 커스텀 한 알림창
    function customAlert(message){
        return new Promise((resolve) => {
            const alertDiv = document.createElement('div');
            alertDiv.className = 'custom-alert';
            alertDiv.innerHTML = `
                <div class="message">${message}</div>
                <button class="close-btn">확인</button>
            `;
            document.body.appendChild(alertDiv);

            // 약간의 지연 후 show 클래스 추가
            setTimeout(() => {
                alertDiv.classList.add('show');
            }, 10);

            alertDiv.querySelector('.close-btn').onclick = function() {
                alertDiv.classList.remove('show');
                setTimeout(() => {
                    alertDiv.remove();
                    resolve();
                }, 300);
            };
        });
    }

    // 로그인 에러 메시지 확인
    const loginError = document.getElementById('adminLoginError')?.value;
    if(loginError) {
        customAlert(loginError);
    }

    // 로그인 폼 제출 시
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const id = loginForm.querySelector('input[name="adminLoginId"]').value.trim();
        const pw = loginForm.querySelector('input[name="adminPassword"]').value.trim();

        if (!id) {
            customAlert('아이디를 입력하세요.');
            return;
        }
        if (!pw) {
            customAlert('비밀번호를 입력하세요.');
            return;
        }

        // 일반 폼 제출
        loginForm.submit();
    });
}); 