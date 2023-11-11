package com.example.springtrash.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.springtrash.member.dto.MemberCreate;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.FixtureMonkeyBuilder;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MemberTest {


    @DisplayName("[회원 생성 - 성공] MemberCreate로 Member를 만들 수 있다.")
    @Test
    void createTest() {
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

    @DisplayName("[로그인 - 성공] 회원은 로그인을 하면 LastLoginDate가 변경된다.")
    @Test
    void loginTest(){
        // given
        LocalDateTime test = LocalDateTime.now();
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();
        Member member = Member.create(dto);

        // when
        Member sut = member.login();

        // then
        assertThat(sut.getLastLoginDate()).isAfterOrEqualTo(test);
        assertThat(sut.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(sut.getEmail()).isEqualTo(member.getEmail());
        assertThat(sut.getNickname()).isEqualTo(member.getNickname());
        assertThat(sut.getName()).isEqualTo(member.getName());
        assertThat(sut.getPassword()).isEqualTo(member.getPassword());
        assertThat(sut.getRole()).isEqualTo(member.getRole());
        assertThat(sut.getStatus()).isEqualTo(member.getStatus());
        assertThat(sut.getJoinDate()).isEqualTo(member.getJoinDate());
    }


    @Test
    void fixtureMonkeyTest () throws Exception{
        //given
        LocalDateTime test = LocalDateTime.now();
        FixtureMonkey monkey = FixtureMonkey.builder()
                .plugin(new JavaxValidationPlugin())
                .build();
        MemberCreate dto = monkey.giveMeOne(MemberCreate.class);
        System.out.println(dto);
        //when
        Member member = Member.create(dto);

        // when
        Member sut = member.login();


        // then
        assertThat(sut.getLastLoginDate()).isAfterOrEqualTo(test);
        assertThat(sut.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(sut.getEmail()).isEqualTo(member.getEmail());
        assertThat(sut.getNickname()).isEqualTo(member.getNickname());
        assertThat(sut.getName()).isEqualTo(member.getName());
        assertThat(sut.getPassword()).isEqualTo(member.getPassword());
        assertThat(sut.getRole()).isEqualTo(member.getRole());
        assertThat(sut.getStatus()).isEqualTo(member.getStatus());
        assertThat(sut.getJoinDate()).isEqualTo(member.getJoinDate());

    }

}