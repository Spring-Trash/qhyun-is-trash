package com.example.springtrash.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.domain.MemberRole;
import com.example.springtrash.member.domain.MemberStatus;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.exception.MemberErrorCode;
import com.example.springtrash.member.service.port.MemberRepository;
import com.example.springtrash.mock.FakeMemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MemberServiceTest {


    private MemberService memberService;

    // TODO 정보 조회 기능이 생성되면 ServiceTest에서 Repository 변수 제거
    private MemberRepository memberRepository;
    @BeforeEach
    void setUp(){
        memberRepository = new FakeMemberRepository();
        memberService = new MemberServiceImpl(memberRepository);
    }
    @DisplayName("[회원 가입 - 성공]")
    @Test
    void joinSuccessTest() {
        //given
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();

        // when
        memberService.join(dto);
        Optional<Member> sut = memberRepository.findById(1);
        // then
        assertThat(sut).isNotEmpty();
        Member member = sut.get();
        assertThat(member.getLoginId()).isEqualTo("foo");
        assertThat(member.getEmail()).isEqualTo("foo@bar.com");
        assertThat(member.getName()).isEqualTo("bar");
        assertThat(member.getNickname()).isEqualTo("foobar");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
        assertThat(member.getJoinDate()).isNotNull();
    }

    @DisplayName("[회원 가입 - 실패] 아이디가 중복이다.")
    @Test
    void joinFailLoginIdDuplicateTest() {
        //given
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();
        memberService.join(dto);

        // when
        assertThatThrownBy(() -> memberService.join(dto))
                .isInstanceOf(ConflictException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.LOGIN_ID_DUPLICATED);
    }
}