package com.example.springtrash.member.controller;

import static com.example.springtrash.common.exception.ServerErrorCode.SESSION_INFO_LOST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.common.exception.GlobalErrorCode;
import com.example.springtrash.common.exception.ServerErrorCode;
import com.example.springtrash.common.exception.ServerException;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.controller.response.MemberInfo;
import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.domain.MemberRole;
import com.example.springtrash.member.domain.MemberStatus;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import com.example.springtrash.member.dto.MemberUpdate;
import com.example.springtrash.member.exception.MemberErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private MemberService memberService;


    @DisplayName("[회원 가입 - 성공]")
    @Test
    void signUpSuccess() throws Exception {
        //given
        String content = mapper.writeValueAsString(MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build());

        //when
        ResultActions sut = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isCreated())
                .andDo(print());
    }
    // Field Error

    @DisplayName("[회원 가입 - 실패] 필드가 Validation을 지키지 않았다.")
    @Test
    void signUpFailValidationFailTest() throws Exception {
        //given
        String content = mapper.writeValueAsString(MemberCreate.builder()
                .loginId("")
                .email(".com")
                .name("")
                .nickname("")
                .password("!!")
                .build());

        //when
        ResultActions sut = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.error").value(GlobalErrorCode.INVALID_FIELD_INPUT.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INVALID_FIELD_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(
                        GlobalErrorCode.INVALID_FIELD_INPUT.getMessage()))
                .andDo(print());
    }


    //     IdDuplicated
    @DisplayName("[회원 가입 - 실패] 로그인 아이디가 중복되었다.")
    @Test
    void signUpFailLoginIdDuplicatedTest() throws Exception {
        //given
        doThrow(new ConflictException(MemberErrorCode.LOGIN_ID_DUPLICATED))
                .when(memberService)
                .join(any());

        String content = mapper.writeValueAsString(MemberCreate.builder()
                .loginId("foo")
                .email("foo@bar.com")
                .name("bar")
                .nickname("foobar")
                .password("1q2w3e4r!!")
                .build());

        //when
        ResultActions sut = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(
                        jsonPath("$.error").value(MemberErrorCode.LOGIN_ID_DUPLICATED.getError()))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.LOGIN_ID_DUPLICATED.getCode()))
                .andExpect(jsonPath("$.message").value(
                        MemberErrorCode.LOGIN_ID_DUPLICATED.getMessage()))
                .andDo(print());
    }


    /*
        로그인 테스트.
        로그인을 성공하면 200 status. session에 회원 정보가 추가되어야 한다.
        로그인을 실패하는 케이스는 2가지.
        1. 입력한 id,password가 양식을 지키지 않는 경우.
        2. 입력한 id,password가 저장된 정보와 다른 경우이다.
     */

    @DisplayName("[로그인 - 성공] 유효한 id, password를 입력하면, session에 정보가 담긴다. ")
    @Test
    void loginSuccessTest() throws Exception {
        //given
        Member dummyMember = Member.builder().loginId("foo")
                .nickname("bao")
                .build();

        given(memberService.login(any()))
                .willReturn(dummyMember);

        MemberLogin request = MemberLogin.builder()
                .loginId("foo")
                .password("bar")
                .build();
        String content = mapper.writeValueAsString(request);

        MemberSession memberSession = MemberSession.fromEntity(dummyMember);
        //when
        ResultActions sut = mvc.perform(
                post("/members/login").contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        //then
        sut.andExpect(status().isOk())
                .andExpect(request().sessionAttribute("userInfo", memberSession))
                .andDo(print());
    }

    @DisplayName("[로그인 - 실패] 유효하지 않은 id, password를 입력하면, INVALID_FIELD_INPUT 문제가 발생한다. ")
    @Test
    void loginFailInvalidInputTest() throws Exception {
        //given

        MemberLogin request = MemberLogin.builder()
                .loginId("")
                .password(null)
                .build();
        String content = mapper.writeValueAsString(request);

        //when
        ResultActions sut = mvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.error").value(GlobalErrorCode.INVALID_FIELD_INPUT.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INVALID_FIELD_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(
                        GlobalErrorCode.INVALID_FIELD_INPUT.getMessage()))
                .andDo(print());
    }

    @DisplayName("[로그인 - 실패] 틀린 ID,Password를 입력하면 WrongIdOrPassword 이 발생한다. ")
    @Test
    void loginFailIdWrongPasswordTest() throws Exception {
        //given
        doThrow(new ConflictException(MemberErrorCode.WRONG_ID_OR_PASSWORD))
                .when(memberService)
                .login(any());

        MemberLogin request = MemberLogin.builder()
                .loginId("foo")
                .password("bar")
                .build();
        String content = mapper.writeValueAsString(request);
        //when
        ResultActions sut = mvc.perform(
                post("/members/login").contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        //then
        sut.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value(MemberErrorCode.WRONG_ID_OR_PASSWORD.getError()))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.WRONG_ID_OR_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(
                        MemberErrorCode.WRONG_ID_OR_PASSWORD.getMessage()))
                .andDo(print());
    }


/*
        내 정보 조회
        0. 현재 접근에 세션이 존재하는지 체크한다.
        1. Session 정보 (로그인 아이디, 닉네임) 을 통해 정보를 조회한다.
*/

    @DisplayName("[내 정보 조회 - 성공] Session 정보를 통해 내 정보를 조회할 수 있다")
    @Test
    void retrieveMyInfoSuccessTest() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("foobao")
                .loginId("foobar")
                .nickname("foobao")
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.USER)
                .email("foo@bar.com")
                .statusMessage("i am hungry")
                .password("1q2w3e4r!!")
                .joinDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .build();
        MemberSession userInfo = MemberSession.builder()
                .loginId("foobar")
                .nickname("foobao")
                .build();
        given(memberService.retrieveMyInfo(any()))
                .willReturn(member);


        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userInfo", userInfo);
        //when
        MemberInfo response = MemberInfo.fromEntity(member);
        ResultActions sut = mvc.perform(get("/members/my")
                .session(session));
        //then
        sut
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value(response.getLoginId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.statusMessage").value(response.getStatusMessage()))
                .andDo(print());
    }


    @DisplayName("[내 정보 조회 - 실패] 세션 정보가 없다면 Filter를 통해 걸러진다.")
    @Test
    void retrieveMyInfoFailNoSessionTest() throws Exception {
        //given
        //when
        ResultActions sut = mvc.perform(get("/members/my"));
        //then
        sut
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value(GlobalErrorCode.UN_AUTHORIZED_ACCESS.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.UN_AUTHORIZED_ACCESS.getCode()))
                .andExpect(jsonPath("$.message").value(
                        GlobalErrorCode.UN_AUTHORIZED_ACCESS.getMessage()))
                .andDo(print());
    }


    @DisplayName("[내 정보 조회 - 실패] Session 정보가 DB에 존재하지 않는 경우")
    @Test
    void retrieveMyInfoFailSessionDataLostTest() throws Exception {
        //given
        MemberSession userInfo = MemberSession.builder()
                .loginId("foobar")
                .nickname("foobao")
                .build();
        given(memberService.retrieveMyInfo(any()))
                .willThrow(new ServerException(SESSION_INFO_LOST));


        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userInfo", userInfo);
        //when
        ResultActions sut = mvc.perform(get("/members/my")
                .session(session));
        //then
        sut
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value(SESSION_INFO_LOST.getError()))
                .andExpect(jsonPath("$.code").value(SESSION_INFO_LOST.getCode()))
                .andExpect(jsonPath("$.message").value(
                        SESSION_INFO_LOST.getMessage()))
                .andDo(print());
    }


  @DisplayName("[회원 정보 수정 - 성공] 닉네임, 비밀번호, 상태메세지 필드에 값이 있으면 정보가 수정된다.")
  @Test
  void modifyMyInfoSuccessTest() throws Exception{
      //given
      MemberSession userInfo = MemberSession.builder()
              .loginId("foobar")
              .nickname("foobao")
              .build();


      MockHttpSession session = new MockHttpSession();
      session.setAttribute("userInfo", userInfo);

      String content = mapper.writeValueAsString(MemberUpdate.builder()
              .nickname("asdf")
              .password("1234")
              .statusMessage("hello")
              .build());
      //when
      mvc.perform(patch("/members/my")
              .contentType(MediaType.APPLICATION_JSON)
              .session(session)
              .content(content)
      )
              .andExpect(status().isOk())
              .andDo(print());
      //then

  }


}