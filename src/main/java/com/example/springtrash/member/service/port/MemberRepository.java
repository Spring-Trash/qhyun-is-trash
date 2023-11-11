package com.example.springtrash.member.service.port;

import com.example.springtrash.member.domain.Member;
import java.util.Optional;

public interface MemberRepository {


    boolean isDuplicateId(String loginId);

    void join(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByLoginId(String loginId);

    void login(Member member);

    void updateMember(Member member);
}
