package com.example.springtrash.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.springtrash.member.dto.MemberCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {


    @DisplayName("[회원 생성 - 성공] MemberCreate로 Member를 만들 수 있다.")
    @Test
    void create() {
        //given
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();

        // when
        Member sut = Member.create(dto);
        // then
        assertThat(sut.getLoginId()).isEqualTo(dto.getLoginId());
        assertThat(sut.getEmail()).isEqualTo(dto.getEmail());
        assertThat(sut.getNickname()).isEqualTo(dto.getNickname());
        assertThat(sut.getName()).isEqualTo(dto.getName());
        assertThat(sut.getPassword()).isEqualTo(dto.getPassword());
        assertThat(sut.getRole()).isEqualTo(MemberRole.USER);
        assertThat(sut.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(sut.getJoinDate()).isNotNull();
    }
}