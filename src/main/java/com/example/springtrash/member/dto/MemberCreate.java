package com.example.springtrash.member.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberCreate {

    @NotNull
    @NotBlank
    private String loginId;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @NotBlank
    private String name;


}
