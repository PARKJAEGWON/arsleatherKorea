package com.groo.kmw.domain.admin.notice.repository;

import com.groo.kmw.domain.admin.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByNoticeStatusOrderByCreateDateTimeDesc(int noticeStatus);

    Page<Notice> findByNoticeStatusOrderByCreateDateTimeDesc(int noticeStatus, Pageable pageable);

    List<Notice> findByNoticeStatusInOrderByCreateDateTimeDesc(List<Integer> noticeStatuses);

}
