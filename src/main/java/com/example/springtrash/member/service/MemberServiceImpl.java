package com.example.springtrash.member.service;

import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

}
