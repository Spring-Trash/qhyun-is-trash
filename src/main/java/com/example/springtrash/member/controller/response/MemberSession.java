package com.example.springtrash.member.controller.response;


import com.example.springtrash.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberSession {

    private String loginId;
    private String nickname;


    public static MemberSession fromEntity(Member member) {
        return MemberSession.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .build();
    }
}
