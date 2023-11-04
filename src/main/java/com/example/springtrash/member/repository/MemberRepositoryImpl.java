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
    public Optional<Member> findById(Integer id) {
        return Optional.empty();
    }
}
