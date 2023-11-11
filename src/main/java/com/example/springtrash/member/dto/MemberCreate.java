package com.example.springtrash.member.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
//    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}")
    private String loginId;

    @NotNull
//    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\^&()-])(?=.*[a-zA-Z]).{8,20}$")
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min=2, max = 12)
    private String nickname;

    @NotNull
    @NotBlank
    @Size(min=2, max = 12)
    private String name;


}
