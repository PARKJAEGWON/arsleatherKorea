document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.login-form');

    //비동기적 커스텀 한 알림창
    function customAlert(message){
        return new Promise((resolve) => {
            const alertDiv =document.createElement('div');
            alertDiv.className = 'custom-alert';
            alertDiv.innerHTML = `
                <div class="message">${message}</div>
                <button class="close-btn">확인</button>
            `;
            document.body.appendChild(alertDiv);

            alertDiv.style.display = 'block';

            alertDiv.querySelector('.close-btn').onclick = function() {
                alertDiv.remove();
                resolve();
            };
        });
    }

    // 확인/취소 커스텀 알림창 (restoreCustomAlert)
    function restoreCustomAlert(message) {
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

    //로그인 실패시 백엔드에 실패코드 가져와서 프론트에도 알림창을 띄움
    const loginError = document.getElementById('loginError').value;
    if(loginError){
        customAlert(loginError);
    }

    // 복구 성공/실패 메시지 알림창
    const message = document.getElementById('message')?.value;
    if (message) {
        customAlert(message);
    }

    // 탈퇴 계정 복구 confirm
    const withdrawnAccount = document.getElementById('withdrawnAccount')?.value;
    const withdrawnLoginId = document.getElementById('withdrawnLoginId')?.value;
    const withdrawnPassword = document.getElementById('withdrawnPassword')?.value;

    if (withdrawnAccount === 'true') {
        // 복구 confirm 알림창
        restoreCustomAlert('탈퇴된 계정입니다. 복구 하시겠습니까?').then((restore) => {
            if (restore) {
                // 복구용 값 세팅
                form.querySelector('input[name="memberLoginId"]').value = withdrawnLoginId;
                form.querySelector('input[name="memberPassword"]').value = withdrawnPassword;
                form.action = '/member/restore'; // 복구 컨트롤러로 전송
                form.submit();
            }
        });
    }

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const id = form.querySelector('input[name="memberLoginId"]').value.trim();
        const pw = form.querySelector('input[name="memberPassword"]').value.trim();

        if (!id) {
            customAlert('아이디(이메일)를 입력하세요.');
            return;
        }
        if (!pw) {
            customAlert('비밀번호를 입력하세요.');
            return;
        }
        form.submit();
    });
});