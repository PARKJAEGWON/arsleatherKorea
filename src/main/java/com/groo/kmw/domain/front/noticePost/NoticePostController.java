package com.groo.kmw.domain.front.noticePost;

import com.groo.kmw.domain.admin.notice.entity.Notice;
import com.groo.kmw.domain.admin.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticePostController {

    private final NoticeService noticeService;

    //공지사항 리스트 보류
    @GetMapping("")
    public String noticeListPage(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size, Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage = noticeService.noticePage(pageable);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticePage.getTotalPages());

        return "front/notice/noticeList";
    }

    @GetMapping("/detail")
    public String noticeDetail(@RequestParam("id")Long noticeId, Model model){
        Notice notice = noticeService.getNotice(noticeId);

        model.addAttribute("notice", notice);

        return "front/notice/noticeDetail";
    }
}
