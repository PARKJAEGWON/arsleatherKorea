package com.groo.kmw.domain.front.member.dto.request;

import com.groo.kmw.domain.front.member.entity.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberSignupRequest {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{3,}$", message = "아이디는 영문으로 시작하고, 영문, 숫자만 사용 가능하며 4자 이상이어야 합니다.")
    private String memberLoginId;

    @NotNull
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",message = "비밀번호는 영문과 숫자를 포함해야 합니다.")
    private String memberPassword;

    @NotNull
    private String memberName;

    @NotNull
    private String memberPhone;

    @NotNull
    private String memberEmail;

    private LocalDate memberBirthDate;

    private Gender memberGender;

    private String memberZipCode;

    private String memberAddress1;

    private String memberAddress2;

    private boolean memberEmailAgree;

    private boolean memberSmsAgree;
}
