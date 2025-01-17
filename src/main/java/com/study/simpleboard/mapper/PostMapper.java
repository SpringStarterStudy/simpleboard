package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    // 게시물 전체 목록 조회
    List<PostDto.Info> selectAllPost(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("searchKeyword") String searchKeyword,
            @Param("searchUser") String searchUser
    );

    // 전체 게시글 수 조회
    long countPosts(@Param("searchKeyword") String searchKeyword, @Param("searchUser") String searchUser);

}
