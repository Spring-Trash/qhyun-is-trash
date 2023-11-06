package com.example.springtrash.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.common.exception.GlobalErrorCode;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.dto.MemberCreate;
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
                .andExpect(jsonPath("$.error").value(GlobalErrorCode.FIELD_VALIDATION_ERROR.getError()))
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.FIELD_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.FIELD_VALIDATION_ERROR.getMessage()))
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


}