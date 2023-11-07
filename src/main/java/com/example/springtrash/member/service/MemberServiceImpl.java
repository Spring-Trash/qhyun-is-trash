package com.example.springtrash.member.service;

import com.example.springtrash.common.exception.ConflictException;
import com.example.springtrash.common.exception.ServerErrorCode;
import com.example.springtrash.common.exception.ServerException;
import com.example.springtrash.member.controller.port.MemberService;
import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import com.example.springtrash.member.exception.MemberErrorCode;
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

    @Override
    public Member login(MemberLogin memberLogin) {


        Member member = memberRepository.findByLoginId(memberLogin.getLoginId())
                .orElseThrow(() -> new ConflictException(MemberErrorCode.WRONG_ID_OR_PASSWORD));

        if(!member.getPassword().equals(memberLogin.getPassword()))
            throw new ConflictException(MemberErrorCode.WRONG_ID_OR_PASSWORD);

        member = member.login();
        memberRepository.login(member);

        return member;
    }

    @Override
    public Member retrieveMyInfo(MemberSession memberSession) {
        return memberRepository.findByLoginId(memberSession.getLoginId())
                .orElseThrow(() -> new ServerException(ServerErrorCode.SESSION_INFO_LOST));
    }

    private void validateJoin(MemberCreate memberCreate) {
        if(memberRepository.isDuplicateId(memberCreate.getLoginId())){
            throw new ConflictException(MemberErrorCode.LOGIN_ID_DUPLICATED);
        }
    }
}
