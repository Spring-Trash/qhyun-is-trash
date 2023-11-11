package com.example.springtrash.member.controller.port;

import com.example.springtrash.member.controller.response.MemberSession;
import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;
import com.example.springtrash.member.dto.MemberLogin;
import com.example.springtrash.member.dto.MemberUpdate;

public interface MemberService {


    void join(MemberCreate memberCreate);

    Member login(MemberLogin memberLogin);

    Member retrieveMyInfo(MemberSession memberSession);

    void updateMyInfo(MemberSession session, MemberUpdate memberUpdate);
}
