package com.groo.kmw.domain.front.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    
    private String memberPassword;

    @Email
    private String memberEmail;

    private String memberPhone;

    private String memberZipCode;

    private String memberAddress1;

    private String memberAddress2;

    private boolean memberSmsAgree;
    private boolean memberEmailAgree;
}
