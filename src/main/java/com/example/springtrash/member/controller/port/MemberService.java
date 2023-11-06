package com.example.springtrash.member.controller.port;

import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;

public interface MemberService {


    void join(MemberCreate memberCreate);

    Member login(MemberLogin memberLogin);
}
