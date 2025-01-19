package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    // 게시물 상세 조회
    Optional<PostDto.Detail> selectPostById(Long postId);

    // 조회수 증가
    void updateViewCount(Long postId, Long viewCount);
}