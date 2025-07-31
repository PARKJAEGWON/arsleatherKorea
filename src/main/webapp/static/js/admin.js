document.addEventListener('DOMContentLoaded', function() {
    // 비밀번호 변경 메시지 확인
    const messageElement = document.getElementById('message');
    if (messageElement && messageElement.textContent.trim()) {
        customAlert(messageElement.textContent.trim());
    }
});

// 비동기적 커스텀 알림창
function customAlert(message) {
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

// 비동기적 커스텀 confirm 얼럿 (확인/취소)
function customConfirm(message) {
    return new Promise((resolve) => {
        const alertDiv = document.createElement('div');
        alertDiv.className = 'custom-alert';
        alertDiv.innerHTML = `
            <div class="message">${message}</div>
            <button class="close-btn" id="confirmBtn">확인</button>
            <button class="close-btn" id="cancelBtn">취소</button>
        `;
        document.body.appendChild(alertDiv);
        setTimeout(() => {
            alertDiv.classList.add('show');
        }, 10);
        alertDiv.querySelector('#confirmBtn').onclick = function() {
            alertDiv.classList.remove('show');
            setTimeout(() => {
                alertDiv.remove();
                resolve(true);
            }, 300);
        };
        alertDiv.querySelector('#cancelBtn').onclick = function() {
            alertDiv.classList.remove('show');
            setTimeout(() => {
                alertDiv.remove();
                resolve(false);
            }, 300);
        };
    });
}

// 비밀번호 변경 모달 표시
function showPasswordChangeModal() {
    const modal = document.createElement('div');
    modal.className = 'password-change-modal';
    modal.innerHTML = `
        <div class="modal-content">
            <h2>비밀번호 변경</h2>
            <form id="passwordChangeForm" action="/admin/kmw/update" method="post">
                <div class="modal-input-group">
                    <input type="password" name="adminPassword" placeholder="새 비밀번호" required>
                    <div class="error-message" id="passwordError"></div>
                </div>
                <div class="modal-input-group">
                    <input type="password" name="adminPasswordConfirm" placeholder="비밀번호 확인" required>
                    <div class="error-message" id="passwordConfirmError"></div>
                </div>
                <div class="modal-button-group">
                    <button type="submit">변경</button>
                    <button type="button" onclick="closePasswordModal()">취소</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);

    // 폼 제출 이벤트 처리
    const form = document.getElementById('passwordChangeForm');
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const password = form.querySelector('input[name="adminPassword"]').value;
        const passwordConfirm = form.querySelector('input[name="adminPasswordConfirm"]').value;
        const passwordError = document.getElementById('passwordError');
        const passwordConfirmError = document.getElementById('passwordConfirmError');
        
        // 비밀번호 유효성 검사
        if (password.length < 8) {
            passwordError.textContent = '비밀번호는 8자 이상이어야 합니다.';
            passwordError.style.display = 'block';
            return;
        }
        
        // 비밀번호 확인 검사
        if (password !== passwordConfirm) {
            passwordConfirmError.textContent = '비밀번호가 일치하지 않습니다.';
            passwordConfirmError.style.display = 'block';
            return;
        }

        // 폼 데이터 전송
        fetch('/admin/kmw/update', {
            method: 'POST',
            body: new FormData(form)
        })
        .then(response => {
            if (response.ok) {
                closePasswordModal();
                customAlert('비밀번호가 성공적으로 변경되었습니다.');
            } else {
                throw new Error('비밀번호 변경에 실패했습니다.');
            }
        })
        .catch(error => {
            customAlert(error.message);
        });
    });
}

// 비밀번호 변경 모달 닫기
function closePasswordModal() {
    const modal = document.querySelector('.password-change-modal');
    if (modal) {
        modal.remove();
    }
}

function showCustomAlert(message, onConfirm) {
    customConfirm(message).then(function(result) {
        if(result && typeof onConfirm === 'function') {
            onConfirm();
        }
    });
} 
// 회원 메모 관련 함수
function showDescriptionEdit() {
    document.getElementById('descriptionDisplay').style.display = 'none';
    document.getElementById('descriptionEdit').style.display = 'block';
}

function cancelDescriptionEdit() {
    document.getElementById('descriptionDisplay').style.display = 'flex';
    document.getElementById('descriptionEdit').style.display = 'none';
}

function saveMemberDescription(button) {
    const memberId = button.getAttribute('data-member-id');
    const description = document.getElementById('memberDescription').value;
    
    fetch(`/admin/kmw/member/description/${memberId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ description: description })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // 화면 업데이트
            document.querySelector('#descriptionDisplay p').textContent = 
                description || '등록된 메모가 없습니다.';
            cancelDescriptionEdit();
        } else {
            alert('저장에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('저장 중 오류가 발생했습니다.');
    });
} 
