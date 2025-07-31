package com.groo.kmw.domain.admin.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NoticeCreateRequest {

    @NotBlank(message = "제목은 필수입니다")
    private String noticeTitle;

    @NotBlank(message = "내용은 필수입니다")
    private String noticeContent;

    private int noticeStatus;

    private Long adminId;

    private List<MultipartFile> NoticeImageUrls;
}
