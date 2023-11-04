package com.example.springtrash.mock;

import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.service.port.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {

    private final Map<Integer, Member> database = new HashMap<>();

    private int autoGeneratedId = 0;

    @Override
    public boolean isDuplicateId(String loginId) {

        for (Member member : database.values()) {
            if(member.getLoginId().equals(loginId))
                return true;
        }
        return false;
    }

    @Override
    public void join(Member member) {
        database.put(++autoGeneratedId, member);
    }

    @Override
    public Optional<Member> findById(Integer id) {
        return Optional.ofNullable(database.get(id));
    }
}