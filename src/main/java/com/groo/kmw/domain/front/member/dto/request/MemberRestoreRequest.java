package com.groo.kmw.domain.front.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRestoreRequest {
    @NotNull
    private String memberLoginId;
    @NotNull
    private String memberPassword;
}
