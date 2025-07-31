// 이용약관 팝업 열기
function openTermsPopup(url) {
    const popupWidth = 500;
    const popupHeight = 600;
    const left = (window.screen.width - popupWidth) / 2;
    const top = (window.screen.height - popupHeight) / 2;
    
    window.open(url, '', `width=${popupWidth},height=${popupHeight},left=${left},top=${top}`);
} 