package com.groo.kmw.domain.front.member.entity;

import com.groo.kmw.domain.front.member.entity.enums.Gender;
import com.groo.kmw.global.baseEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//상속받은곳에서 @NoArgsConstructor(access = AccessLevel.PROTECTED) 사용시 에러 터짐 중복 오류같은데 알아봐야할 것 같음
@SuperBuilder
public class Member extends BaseEntity {

    @Column(nullable = false, length = 50, unique = true)
    private String memberLoginId;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false, length = 50)
    private String memberName;

    @Column(nullable = false)
    private LocalDate memberBirthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender memberGender;

    @Column(nullable = false, length = 15)
    private String memberPhone;

//    @Column(nullable = false)
//    private boolean phoneVerified = false;

    @Column(nullable = false, length = 100, unique = true)
    private String memberEmail;

    @Column(length = 10)
    private String memberZipCode;

    @Column(length = 150)
    private String memberAddress1;

    @Column(length = 150)
    private String memberAddress2;

    @Column(nullable = false)
    private boolean memberEmailAgree =false;

    private LocalDateTime memberEmailAgreeTime;

    @Column(nullable = false)
    private boolean memberSmsAgree =false;

    private LocalDateTime memberSmsAgreeTime;

    // = 0; 부분이 기본으로 0으로 들어가는 코드 그래서 회원가입때 상태코드 안넘김 까먹지 말자
    @Column(nullable = false)
    private int memberStatus = 0; // 0:이용중, 8:회원탈퇴, 9:정지

    private LocalDateTime withdrawDateTime;

    @Column(length = 1000)
    private String memberRefreshToken;

    private String memberDescription;
}
