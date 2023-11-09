package com.example.springtrash.member.infra;


import com.example.springtrash.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapper {

    @Select("select count(*) from members where login_id = #{loginId}")
    int countByLoginId(String loginId);

    @Insert("insert into members (login_id, name, password, nickname, email,  join_date, role, status) "
            + "values (#{loginId}, #{name}, #{password}, #{nickname}, #{email}, #{joinDate}, #{role}, #{status})")
    void join(Member member);


    @Select("select * from members where login_id = #{loginId}")
    Member findByLoginId(String loginId);

    @Update("update members set last_login_date = #{lastLoginDate} where member_id = #{id}")
    void login(Member member);
}
