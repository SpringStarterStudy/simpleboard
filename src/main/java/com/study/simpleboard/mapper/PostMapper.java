package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    // 게시물 수정
    boolean existsById(Long postId);
    void updatePostById(@Param("postId") Long postId, @Param("request") PostDto.UpdateRequest request);
}