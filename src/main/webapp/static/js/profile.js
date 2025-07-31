// 커스텀 알림창 함수 (동적 생성)
function customAlert(message) {
    return new Promise((resolve) => {
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
            resolve();
        };
    });
}

// 커스텀 confirm 함수 (동적 생성, 확인/취소)
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

// 프로필 초기값 저장용 변수
let initialProfile = {};

// 수정 버튼 클릭 시
document.getElementById('modifyBtn').addEventListener('click', function() {
    // 프로필 초기값 저장
    initialProfile = {
        email: document.getElementById('emailText').textContent,
        phone: document.getElementById('phoneText').textContent,
        zip: document.getElementById('zipText').textContent,
        // 주소는 innerHTML로 줄바꿈 보존
        addr: document.getElementById('addrText').innerHTML,
        addr1: document.getElementById('addrInput') ? document.getElementById('addrInput').value : '',
        addr2: document.getElementById('addrDetailInput') ? document.getElementById('addrDetailInput').value : '',
        sms: document.getElementById('smsText').textContent === 'SMS',
        emailAgree: document.getElementById('emailAgreeText').textContent === 'Email'
    };
    
    // 뷰 모드 숨기기
    document.getElementById('emailView').style.display = 'none';
    document.getElementById('phoneView').style.display = 'none';
    document.getElementById('addressView').style.display = 'none';
    document.getElementById('marketingView').style.display = 'none';
    document.getElementById('passwordView').style.display = 'none';
    
    // 편집 모드 표시
    document.getElementById('emailModify').style.display = 'flex';
    document.getElementById('phoneModify').style.display = 'flex';
    document.getElementById('addressModify').style.display = 'flex';
    document.getElementById('marketingModify').style.display = 'flex';
    document.getElementById('passwordModify').style.display = 'flex';
    
    // 버튼 변경
    this.style.display = 'none';
    document.querySelector('.profile-btn.withdraw:not(#cancelBtn)').style.display = 'none';
    document.getElementById('saveBtn').style.display = 'inline-block';
    document.getElementById('cancelBtn').style.display = 'inline-block';
});

// 취소 버튼 클릭 시
document.getElementById('cancelBtn').addEventListener('click', function() {
    window.location.reload();
});

// 저장 버튼 클릭 시
document.getElementById('saveBtn').addEventListener('click', async function() {
    const updateData = {
        memberPassword: document.getElementById('passwordInput').value || '', // 비밀번호가 없으면 빈 문자열
        memberEmail: document.getElementById('emailInput').value || '', // 이메일이 없으면 빈 문자열
        memberPhone: document.getElementById('phoneInput').value || '', // 전화번호가 없으면 빈 문자열
        memberZipCode: document.getElementById('zipInput').value || '', // 우편번호가 없으면 빈 문자열
        memberAddress1: document.getElementById('addrInput').value || '', // 주소가 없으면 빈 문자열
        memberAddress2: document.getElementById('addrDetailInput').value || '', // 상세주소가 없으면 빈 문자열
        memberSmsAgree: document.getElementById('smsInput').checked,
        memberEmailAgree: document.getElementById('emailAgreeInput').checked
    };

    try {
        const formData = new FormData();
        const password = document.getElementById('passwordInput').value.trim();
        
        // 비밀번호가 비어있지 않을 때만 유효성 검사
        if (password !== '') {
            if (password.length < 8) {
                await customAlert('비밀번호는 8자 이상이어야 합니다.');
                return;
            }
            if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(password)) {
                await customAlert('비밀번호는 영문과 숫자를 포함해야 합니다.');
                return;
            }
        }
        
        formData.append('memberPassword', password);
        formData.append('memberEmail', updateData.memberEmail);
        formData.append('memberPhone', updateData.memberPhone);
        formData.append('memberZipCode', updateData.memberZipCode);
        formData.append('memberAddress1', updateData.memberAddress1);
        formData.append('memberAddress2', updateData.memberAddress2);
        formData.append('memberSmsAgree', String(updateData.memberSmsAgree));
        formData.append('memberEmailAgree', String(updateData.memberEmailAgree));

        const response = await fetch('/member/profile', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
            },
            body: formData
        });

        // JSP 모델 방식이므로 페이지를 새로고침
        window.location.reload();
    } catch (error) {
        console.error('Error:', error);
        await customAlert('회원정보 수정 중 오류가 발생했습니다.');
    }
});

document.getElementById('zipSearchBtn').addEventListener('click', function() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('zipInput').value = data.zonecode;
            document.getElementById('addrInput').value = data.roadAddress || data.jibunAddress;
            document.getElementById('addrDetailInput').focus();
        }
    }).open();
});

// 탈퇴하기 버튼 클릭 시 커스텀 confirm
const withdrawBtn = document.getElementById('withdrawBtn');
if (withdrawBtn) {
    withdrawBtn.addEventListener('click', async function() {
        const confirmed = await examineCustomAlert('정말 탈퇴하시겠습니까?');
        if (confirmed) {
            const withdrawForm = document.getElementById('withdrawForm');
            if (withdrawForm) {
                // fetch로 서버에 탈퇴 요청
                await fetch(withdrawForm.action, {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content'),
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams(new FormData(withdrawForm))
                });
                await customAlert('탈퇴가 완료되었습니다.<br>14일 이내 복구 가능하며 이후 정보는 완전히 삭제됩니다.');
                window.location.href = '/';
            }
        }
    });
}