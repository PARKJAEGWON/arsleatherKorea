package com.groo.kmw.domain.admin.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpadateRequest {
    @NotNull
    private String adminPassword;
}
