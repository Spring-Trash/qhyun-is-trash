package com.example.springtrash.member.controller;


import com.example.springtrash.member.controller.port.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

}
