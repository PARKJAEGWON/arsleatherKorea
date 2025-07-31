package com.groo.kmw.domain.admin.notice.entity;

import com.groo.kmw.domain.admin.admin.entity.Admin;
import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String noticeTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String noticeContent;

    private int noticeStatus = 0; //0.일반 1.최상위 9.숨김


    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeImageUrl> noticeImageUrls = new ArrayList<>();


}
