package com.groo.kmw.domain.admin.admin.entity;

import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Admin extends BaseEntity {

    @Column(nullable = false, length = 50, unique = true)
    private String adminLoginId;

    @Column(nullable = false)
    private String adminPassword;

    @Column(nullable = false, length = 50)
    private String adminName;

    @Column(length = 1000)
    private String adminRefreshToken;

    @Column(nullable = false)
    private int adminStatus = 1; // 0:이용중, 1:승인 대기중, 8:회원탈퇴, 9:정지
}
