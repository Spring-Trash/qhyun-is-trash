package com.example.springtrash.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.common.exception.ServerErrorCode;
import com.example.springtrash.common.exception.ServerException;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.domain.MemberRole;
import com.example.springtrash.member.domain.MemberStatus;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import com.example.springtrash.member.exception.MemberErrorCode;
import com.example.springtrash.member.service.port.MemberRepository;
import com.example.springtrash.mock.FakeMemberRepository;
import java.time.LocalDateTime;
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
        Optional<Member> sut = memberRepository.findByLoginId("foo");
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


    @DisplayName("[로그인 성공] 적절한 ID, PASSWORD를 입력하면 로그인에 성공한다.")
    @Test
    void loginSuccessTest(){
        // given

        LocalDateTime now = LocalDateTime.now();
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();
        memberService.join(dto);

        MemberLogin request = MemberLogin.builder()
                .loginId("foo")
                .password("1q2w3e4r!!")
                .build();

        // when
        Member sut = memberService.login(request);


        // then
        assertThat(sut.getLoginId()).isEqualTo("foo");
        assertThat(sut.getEmail()).isEqualTo("foo@bar.com");
        assertThat(sut.getName()).isEqualTo("bar");
        assertThat(sut.getNickname()).isEqualTo("foobar");
        assertThat(sut.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(sut.getRole()).isEqualTo(MemberRole.USER);
        assertThat(sut.getLastLoginDate()).isAfterOrEqualTo(now);
        assertThat(sut.getJoinDate()).isNotNull();
    }


    @DisplayName("[로그인 실패] ID와 PASSWORD가 맞지 않으면 예외가 터진다.")
    @Test
    void loginFailWrongIdOrPasswordTest(){
        // given
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();
        memberService.join(dto);

        MemberLogin request = MemberLogin.builder()
                .loginId("foo")
                .password("1q2w3e4r")
                .build();

        // when
        assertThatThrownBy(() ->memberService.login(request))
                .isInstanceOf(ConflictException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.WRONG_ID_OR_PASSWORD);
    }



    // 내 정보 조회
    @DisplayName("[내 정보 조회] - 세션 정보로 회원 정보를 얻을 수 있다.")
    @Test
    void retrieveMyInfoSuccessTest () throws Exception{
        //given
        MemberCreate dto = MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build();
        memberService.join(dto);

        MemberSession session = MemberSession.builder()
                .loginId("foo")
                .nickname("foobar")
                .build();

        //when
        Member sut = memberService.retrieveMyInfo(session);
        //then
        assertThat(sut.getLoginId()).isEqualTo(session.getLoginId());
        assertThat(sut.getNickname()).isEqualTo(session.getNickname());
        assertThat(sut.getName()).isEqualTo(dto.getName());
        assertThat(sut.getEmail()).isEqualTo(dto.getEmail());
    }


    @DisplayName("[내 정보 조회 - 실패] 세션 정보가 DB에 존재하지 않는다.")
    @Test
    void retrieveMyInfoFailSessionInfoLostTest(){
        // given

        MemberSession session = MemberSession.builder()
                .loginId("foo")
                .nickname("foobar")
                .build();

        // when
        assertThatThrownBy(() ->memberService.retrieveMyInfo(session))
                .isInstanceOf(ServerException.class)
                .hasFieldOrPropertyWithValue("errorCode", ServerErrorCode.SESSION_INFO_LOST);
    }


}