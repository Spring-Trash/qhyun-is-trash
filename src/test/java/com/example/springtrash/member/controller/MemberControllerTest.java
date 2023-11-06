package com.example.springtrash.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.common.exception.GlobalErrorCode;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import com.example.springtrash.member.exception.MemberErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
    void signUpSuccess () throws Exception{
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
    void signUpFailValidationFailTest () throws Exception{
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
                .andExpect(jsonPath("$.error").value(GlobalErrorCode.INVALID_FIELD_INPUT.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INVALID_FIELD_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_FIELD_INPUT.getMessage()))
                .andDo(print());
    }


//     IdDuplicated
    @DisplayName("[회원 가입 - 실패] 로그인 아이디가 중복되었다.")
    @Test
    void signUpFailLoginIdDuplicatedTest  () throws Exception{
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
                .andExpect(jsonPath("$.error").value(MemberErrorCode.LOGIN_ID_DUPLICATED.getError()))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.LOGIN_ID_DUPLICATED.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.LOGIN_ID_DUPLICATED.getMessage()))
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
    void loginSuccessTest() throws Exception{
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
        ResultActions sut = mvc.perform(post("/members/login").contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isOk())
            .andExpect(request().sessionAttribute("userInfo",memberSession))
            .andDo(print());
    }

    @DisplayName("[로그인 - 실패] 유효하지 않은 id, password를 입력하면, INVALID_FIELD_INPUT 문제가 발생한다. ")
    @Test
    void loginFailInvalidInputTest() throws Exception{
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
                .andExpect(jsonPath("$.error").value(GlobalErrorCode.INVALID_FIELD_INPUT.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INVALID_FIELD_INPUT.getCode()))
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_FIELD_INPUT.getMessage()))
                .andDo(print());
    }

    @DisplayName("[로그인 - 실패] 틀린 ID,Password를 입력하면 WrongIdOrPassword 이 발생한다. ")
    @Test
    void loginFailIdWrongPasswordTest() throws Exception{
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
        ResultActions sut = mvc.perform(post("/members/login").contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        sut.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value(MemberErrorCode.WRONG_ID_OR_PASSWORD.getError()))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.WRONG_ID_OR_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.WRONG_ID_OR_PASSWORD.getMessage()))
                .andDo(print());
    }


}