package com.study.simpleboard.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    boolean existsById(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deletePostById(Long postId, Long userId);

}