$(document).ready(function() {
    // Summernote 초기화
    initializeSummernote();
    
    // 체크박스 이벤트 처리
    initializeCheckbox();
});

function initializeSummernote() {
    $('#noticeContent').summernote({
        height: 800,
        lang: 'ko-KR',
        placeholder: '내용을 입력하세요.',
        callbacks: {
            onImageUpload: function(files) {
                uploadImages(files);
            }
        }
    });
}

function uploadImages(files) {
    for (let i = 0; i < files.length; i++) {
        let formData = new FormData();
        formData.append('file', files[i]);
        
        $.ajax({
            url: '/admin/kmw/notice/uploadImage',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function() {
                $('#noticeContent').summernote('disable');
            },
            complete: function() {
                $('#noticeContent').summernote('enable');
            },
            success: function(response) {
                $('#noticeContent').summernote('insertImage', response.url);
            },
            error: function() {
                alert('이미지 업로드에 실패했습니다.');
            }
        });
    }
}

function initializeCheckbox() {
    $('#noticeTop').on('change', function() {
        $('#noticeStatus').val($(this).is(':checked') ? '1' : '0');
    });
} 