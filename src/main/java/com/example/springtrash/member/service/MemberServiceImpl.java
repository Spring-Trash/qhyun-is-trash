package com.example.springtrash.member.service;

import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.exception.MemberException;
import com.example.springtrash.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void join(MemberCreate memberCreate) {
        // id duplicate check
        validateJoin(memberCreate);
        // join
        memberRepository.join(Member.create(memberCreate));
    }

    private void validateJoin(MemberCreate memberCreate) {
        if(memberRepository.isDuplicateId(memberCreate.getLoginId())){
            throw MemberException.LOGIN_ID_DUPLICATE_EXCEPTION;
        }
    }
}
