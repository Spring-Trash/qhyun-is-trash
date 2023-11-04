package com.example.springtrash.member.infra;


import com.example.springtrash.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

    @Select("select count(*) from members where login_id = #{loginId}")
    int countByLoginId(String loginId);

    @Insert("insert into members (login_id, name, password, nickname, email,  join_date, role, status) "
            + "values (#{loginId}, #{name}, #{password}, #{nickname}, #{email}, #{joinDate}, #{role}, #{status})")
    void join(Member member);

}
