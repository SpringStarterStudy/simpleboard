package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper { // 기본적인 CRUD 작업
    // 회원 가입
    void insertUser(User userDTO);

    // 중복 체크
    boolean existsByName(String name);
    boolean existsByEmail(String email);

    // 회원 조회 (로그인)
    User findByEmail(String email);

    // 회원 정보 조회
    User findById(Long userId);

    // 회원 정보 수정
    void updateUser(User user);

    // 이름 중복 체크 (본인 제외)
    boolean existsByNameAndNotId(@Param("name") String name, @Param("userId") Long userId);

    // 회원 비밀번호 수정

    // 회원 탈퇴

}
