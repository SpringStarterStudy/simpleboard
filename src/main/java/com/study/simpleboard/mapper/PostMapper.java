package com.study.simpleboard.mapper;

import com.study.simpleboard.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    // 게시물 수정
    boolean existsById(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void updatePostById(@Param("postId") Long postId, @Param("post") Post post);
}