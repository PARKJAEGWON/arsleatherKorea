// findId.js 수정
document.addEventListener('DOMContentLoaded', function() {
    // 결과 영역이 있고 성공/실패 메시지가 있으면 결과 영역 표시
    const resultArea = document.getElementById('resultArea');
    if(resultArea && (resultArea.querySelector('.masked-id') || resultArea.querySelector('.error-message'))) {
        document.getElementById('searchForm').style.display = 'none';
        resultArea.style.display = 'block';
    }
});

function showSearchForm() {
    document.getElementById('searchForm').style.display = 'block';
    document.getElementById('resultArea').style.display = 'none';
}