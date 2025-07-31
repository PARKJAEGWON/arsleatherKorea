package com.groo.kmw.domain.admin.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {
    @NotNull
    private String adminLoginId;
    @NotNull
    private String adminPassword;
}
