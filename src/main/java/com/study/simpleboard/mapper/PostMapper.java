package com.study.simpleboard.mapper;

import com.study.simpleboard.domain.Post;
import com.study.simpleboard.dto.response.PostResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<PostResponseDTO.PostList> selectAllPosts(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("searchKeyword") String searchKeyword,
            @Param("searchUser") String searchUser
    );

    long countPosts(String searchKeyword, String searchUser);

    void save(@Param("post") Post post);

    Optional<PostResponseDTO.PostDetail> selectPostById(Long postId);

    void updateViewCount(Long postId);

    Optional<Long> findPostAuthor(Long postId);

    void updatePostById(Long postId, @Param("post") Post post);

    void deletePostById(Long postId);
}