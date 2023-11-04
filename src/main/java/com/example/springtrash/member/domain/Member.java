package com.example.springtrash.member.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


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
    private String statusMessage;
    private LocalDateTime joinDate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastModifiedDate;
    private MemberRole role;
    private MemberStatus status;


}
