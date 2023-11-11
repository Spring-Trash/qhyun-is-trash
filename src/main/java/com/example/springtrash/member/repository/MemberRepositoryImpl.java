package com.example.springtrash.member.repository;

import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.infra.MemberMapper;
import com.example.springtrash.member.service.port.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public boolean isDuplicateId(String loginId) {
        return memberMapper.countByLoginId(loginId) == 1;
    }

    @Override
    public void join(Member member) {
        memberMapper.join(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return Optional.ofNullable(memberMapper.findByLoginId(loginId));
    }

    @Override
    public void login(Member member) {
        memberMapper.login(member);
    }

    @Override
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }
}
