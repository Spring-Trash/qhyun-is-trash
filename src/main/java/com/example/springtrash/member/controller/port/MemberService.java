package com.example.springtrash.member.controller.port;

import com.example.springtrash.member.domain.Member;
import com.example.springtrash.member.dto.MemberCreate;

public interface MemberService {


    void join(MemberCreate memberCreate);

}
