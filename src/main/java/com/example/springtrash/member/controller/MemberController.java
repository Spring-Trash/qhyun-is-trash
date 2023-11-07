package com.example.springtrash.member.controller;


import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.controller.response.MemberInfo;
import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.SessionAttribute;


@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Validated MemberCreate memberCreate) {

        memberService.join(memberCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated MemberLogin memberLogin, HttpSession session) {
        session.setAttribute("userInfo", MemberSession.fromEntity(memberService.login(memberLogin)));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/my")
    public ResponseEntity<MemberInfo> retrieveMyInfo(@SessionAttribute("userInfo") MemberSession memberSession) {
        log.info("session : {}", memberSession);
        return ResponseEntity.ok(MemberInfo.fromEntity(memberService.retrieveMyInfo(memberSession)));

    }
}
