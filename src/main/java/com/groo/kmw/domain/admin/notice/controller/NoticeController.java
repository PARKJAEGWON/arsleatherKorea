package com.groo.kmw.domain.admin.notice.controller;

import com.groo.kmw.domain.admin.notice.dto.request.NoticeCreateRequest;
import com.groo.kmw.domain.admin.notice.dto.request.NoticeUpdateRequest;
import com.groo.kmw.domain.admin.notice.entity.Notice;
import com.groo.kmw.domain.admin.notice.service.NoticeService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/kmw/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtProvider jwtProvider;

    //게시글 목록
    @GetMapping("")
    public String noticeListPage(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "15")int size, Model model){
        List<Notice> allNotices = noticeService.adminNoticePage();

        // 전체 개수
        int totalSize = allNotices.size();
        int totalPages = (int) Math.ceil((double) totalSize / size);

        // 현재 페이지에 해당하는 데이터만 추출
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalSize);
        List<Notice> currentPageNotices = allNotices.subList(startIndex, endIndex);

        // Page 객체로 감싸기
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage = new PageImpl<>(currentPageNotices, pageable, totalSize);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        return "admin/notice/adminNoticeList";
    }

    //게시글 상세페이지
    @GetMapping("detail")
    public String noticeDetailPage(@RequestParam("id")Long noticeId, Model model){
        Notice notice = noticeService.getNotice(noticeId);
        model.addAttribute("notice", notice);
        return "admin/notice/adminNoticeDetail";
    }

    //게시글 생성페이지
    @GetMapping("/create")
    public String noticeCreatePage(){
        return "admin/notice/adminNoticeCreate";
    }

    //게시글 생성
    @PostMapping("/create")
    public String createNotice(@Valid @ModelAttribute NoticeCreateRequest noticeCreateRequest, HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();
        Long adminId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminAccessToken")) {
                    String token = cookie.getValue();

                    //토큰 유효성 검증으로 보완성을 높여주는게 좋음 습관들여야함
                    if (!jwtProvider.verify(token)) {
                        return "redirect:/admin/kmw/login";
                    }
                    //페이로드안에 있는 json형식의 "" 부분이 키 값이 오브젝트
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    adminId = Long.valueOf(claims.get("adminId").toString());
                    break;
                }
            }
        }
        if (adminId == null) {
            return "redirect:/admin/kmw/login";
        }

        try {

            noticeService.create(
//noticeCreateRequest.getAdminId(), 어드민id를 바로 뿌리지않고 쿠키에서 찾아야해서 위에 쿠키에서 꺼낸 adminId를써야지 dto에서 꺼내와서 오류가 있었음
                    adminId,
                    noticeCreateRequest.getNoticeTitle(),
                    noticeCreateRequest.getNoticeContent(),
                    noticeCreateRequest.getNoticeStatus(),
                    noticeCreateRequest.getNoticeImageUrls());
        } catch (Exception e) {
            return "redirect:/admin/kmw/notice/create";
        }
        return "redirect:/admin/kmw/notice";
    }

    //게시글 수정페이지
    @GetMapping("/update")
    public String NoticeUpdatePage(@RequestParam("id")Long noticeId, Model model){
        Notice notice = noticeService.getNotice(noticeId);
        model.addAttribute("notice", notice);
        return "admin/notice/adminNoticeModify";
    }

    //게시글 수정
    @PostMapping("/update")
    public String updateNotice(@RequestParam("id")Long noticeId, @Valid @ModelAttribute NoticeUpdateRequest noticeUpdateRequest, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        Long adminId = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("adminAccessToken")){
                    String token =cookie.getValue();
                    if(!jwtProvider.verify(token)){
                        return "redirect:/admin/kmw/login";
                    }
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    adminId = Long.valueOf(claims.get("adminId").toString());
                    break;
                }
            }
        }

        if(adminId == null){
            return "redirect:/admin/kmw/login";
        }
        try {
            noticeService.update(
                    noticeId,
                    adminId,
                    noticeUpdateRequest.getNoticeTitle(),
                    noticeUpdateRequest.getNoticeContent(),
                    noticeUpdateRequest.getNoticeStatus(),
                    noticeUpdateRequest.getNoticeImageUrls()
            );
            return "redirect:/admin/kmw/notice/detail?id=" + noticeId;
            }  catch (Exception e) {
            return "redirect:/admin/kmw/notice/update?id=" + noticeId + "&error=" + e.getMessage();
        }
    }

//    @PostMapping("/update")
//    public String updateNotice(){
//
//    }


    //에디터 이미지 업로드 엔드포인트
    @PostMapping("/uploadImage")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("file")MultipartFile multipartFile){
        String imageUrl = noticeService.saveNoticeImageFile(multipartFile);
        return Map.of("url", imageUrl);
    }

    //게시글 삭제
    @PostMapping("/delete")
    public String deleteNotice(@RequestParam("id")Long noticeId){
        try {
            noticeService.delete(noticeId);
            return "redirect:/admin/kmw/notice";
        }catch (RuntimeException e){
            return "redirect:/admin/kmw/notice?error=" + e.getMessage();
        }
    }
}
