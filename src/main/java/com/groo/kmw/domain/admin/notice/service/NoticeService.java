package com.groo.kmw.domain.admin.notice.service;

import com.groo.kmw.domain.admin.admin.entity.Admin;
import com.groo.kmw.domain.admin.admin.service.AdminService;
import com.groo.kmw.domain.admin.notice.entity.Notice;
import com.groo.kmw.domain.admin.notice.entity.NoticeImageUrl;
import com.groo.kmw.domain.admin.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NoticeService {

    //application에서 관리안하고 하드코딩방식의 경로
    private final String UPLOAD_PATH = "D:/kmw/uploads/notice/";

    private final NoticeRepository noticeRepository;
    private final AdminService adminService;

//    //게시글 리스트 페이지모드
//    public Page<Notice> adminNoticePage(Pageable pageable){
//        //jpa에서 인터페이스만드는게 더 쉽겠지만 역순말고 다른 정렬순으로도 할 수 있는 Pageable객체 메소드 만들어봄
//        Pageable sortedPageable = PageRequest.of(
//                pageable.getPageNumber(),
//                pageable.getPageSize(),
//                Sort.by(Sort.Direction.DESC,"createDateTime"));
//        return noticeRepository.findAll(sortedPageable);
//    }

    //게시글 리스트
    public List<Notice> adminNoticePage() {
        List<Notice> topNotices = noticeRepository.findByNoticeStatusOrderByCreateDateTimeDesc(1);
        List<Notice> noticeList = noticeRepository.findByNoticeStatusInOrderByCreateDateTimeDesc(Arrays.asList(0, 9));
        
        List<Notice> allNotices = new ArrayList<>();
        
        allNotices.addAll(topNotices);
        allNotices.addAll(noticeList);
        
        return allNotices;
    }

    //게시글 상세페이지
    public Notice getNotice(Long noticeId){
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        if(optionalNotice.isEmpty()){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        Notice notice = optionalNotice.get();

        return notice;
    }

    //파일 저장
    public String saveNoticeImageFile(MultipartFile multipartFile){

        if(multipartFile == null || multipartFile.isEmpty())
            return null;

        try {
            //디렉토리 생성 로직 추가 배포시나 다른 개발자가 내코드를 받아쓸때 폴더가 없을 수 있음
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new RuntimeException("업로드 디렉토리 생성 실패");
                }
            }
        //업로드 된 파일의 원본 이름을 가져오기
        String originalName = multipartFile.getOriginalFilename();
        //같은 이름의 파일의 충돌 방지를 위한 uuid 생성
        String uuid = UUID.randomUUID().toString();
        //***********원본 파일명에서 확장자 부분만 추출 어떤 처리인지는 잘 모르겠음 찾아봐야함************
        String extension = originalName.substring(originalName.lastIndexOf("."));
        //저장될 파일 이름을 uuid + 확장자 형태로 생성
        String savedName = uuid + extension;

            File destFile = new File(uploadDir, savedName);
            multipartFile.transferTo(destFile);

            return "/uploads/notice/" + savedName;
        }catch (IOException e){
            throw new RuntimeException("파일 저장 실패:" + e.getMessage());
        }
    }


    //게시글 생성
    public Notice create(Long adminId, String noticeTitle, String noticeContent, int noticeStatus, List<MultipartFile> noticeImageUrls){

        Notice notice = new Notice();

        Admin admin = adminService.findById(adminId);

        notice.setAdmin(admin);
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        notice.setNoticeStatus(noticeStatus);

       if(noticeImageUrls != null){
           for(MultipartFile file : noticeImageUrls) {
               String url = saveNoticeImageFile(file);
               if (url != null) {
                   NoticeImageUrl imageUrl = new NoticeImageUrl();
                   imageUrl.setImageUrl(url);
                   imageUrl.setNotice(notice); // 양방향 관계설정
                   notice.getNoticeImageUrls().add(imageUrl);
               }
           }
       }
        return noticeRepository.save(notice);
    }

    //게시글 삭제
    public void delete(Long noticeId){
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        if(optionalNotice.isEmpty()){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        Notice notice = optionalNotice.get();
        noticeRepository.delete(notice);
    }
    //게시글 수정
    public void update(Long noticeId, Long adminId, String title, String content,
                       int status, List<MultipartFile> noticeImageUrls){

        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        if(optionalNotice.isEmpty()){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        Notice notice = optionalNotice.get();

        if(!notice.getAdmin().getId().equals(adminId)){
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        notice.setNoticeTitle(title);
        notice.setNoticeContent(content);
        notice.setNoticeStatus(status);

        notice.getNoticeImageUrls().clear();

        if (noticeImageUrls != null){
            for (MultipartFile file : noticeImageUrls){
                String url = saveNoticeImageFile(file);
                if (url != null){
                    NoticeImageUrl imageUrl = new NoticeImageUrl();
                    imageUrl.setImageUrl(url);
                    imageUrl.setNotice(notice);
                    notice.getNoticeImageUrls().add(imageUrl);
                }
            }
        }

        noticeRepository.save(notice);
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //프론트 게시글 리스트
    public Page<Notice> noticePage(Pageable pageable){
        List<Notice> topNotices = noticeRepository.findByNoticeStatusOrderByCreateDateTimeDesc(1);
        int topCount = topNotices.size();
        //조정된
        int adjustedSize = pageable.getPageSize() - topCount;
        Pageable adjustedPageable = PageRequest.of(
                pageable.getPageNumber(),
                adjustedSize);
        Page<Notice> normalNotices = noticeRepository.findByNoticeStatusOrderByCreateDateTimeDesc(0, adjustedPageable);
        List<Notice> combinedContent = new ArrayList<>();
        combinedContent.addAll(topNotices);
        combinedContent.addAll(normalNotices.getContent());

        //새로운 객체를 생성해서 리스트와 페이지 객체를 합침
        return new PageImpl<>(combinedContent, pageable, normalNotices.getTotalElements() + topCount);
    }
}
