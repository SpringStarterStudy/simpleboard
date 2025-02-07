package com.study.simpleboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.study.simpleboard.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostMapper {
    // 게시물 전체 목록 조회
    List<PostDto.ListInfo> selectAllPosts(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("searchKeyword") String searchKeyword,
            @Param("searchUser") String searchUser
    );

    // 전체 게시글 수 조회
    long countPosts(@Param("searchKeyword") String searchKeyword, @Param("searchUser") String searchUser);
  
    // 게시물 상세 조회
    Optional<PostDto.PostResponse> selectPostById(Long postId);

    // 조회수 증가
    void updateViewCount(Long postId);
    
    // 게시물 존재 여부
    boolean existsById(Long postId);
    // 게시물 작성자 확인
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    // 게시물 삭제 (soft-delete)
    void deletePostById(Long postId, Long userId);

}
