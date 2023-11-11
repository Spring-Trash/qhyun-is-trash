package com.example.springtrash.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberLogin {

    @NotNull
    @NotBlank
    private String loginId;

    @NotNull
    @NotBlank
    private String password;
}
