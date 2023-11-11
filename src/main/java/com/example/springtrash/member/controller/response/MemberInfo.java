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
public class MemberInfo {

    private String loginId;
    private String name;
    private String nickname;
    private String statusMessage;


    public static MemberInfo fromEntity(Member member) {
        return MemberInfo.builder()
                .loginId(member.getLoginId())
                .name(member.getName())
                .nickname(member.getNickname())
                .statusMessage(member.getStatusMessage())
                .build();
    }
}
