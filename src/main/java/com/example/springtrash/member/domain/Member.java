package com.example.springtrash.member.domain;

import com.example.springtrash.member.dto.MemberCreate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String name;
    private String statusMessage;
    private LocalDateTime joinDate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastModifiedDate;
    private MemberRole role;
    private MemberStatus status;



    public static Member create(MemberCreate memberCreate) {
        return Member.builder()
                .loginId(memberCreate.getLoginId())
                .email(memberCreate.getEmail())
                .nickname(memberCreate.getNickname())
                .password(memberCreate.getPassword())
                .name(memberCreate.getName())
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .joinDate(LocalDateTime.now())
                .build();
    }

    public Member login() {
        return Member.builder()
                .loginId(this.getLoginId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .password(this.getPassword())
                .name(this.getName())
                .role(this.getRole())
                .status(this.getStatus())
                .joinDate(this.getJoinDate())
                .lastLoginDate(LocalDateTime.now())
                .lastModifiedDate(this.getLastModifiedDate())
                .build();
    }
}
