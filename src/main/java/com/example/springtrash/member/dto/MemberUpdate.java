package com.example.springtrash.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberUpdate {

    private String nickname;
    private String password;
    private String statusMessage;

}
