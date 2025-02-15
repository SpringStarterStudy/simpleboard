package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.study.simpleboard.dto.PostDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.study.simpleboard.domain.Post;
import com.study.simpleboard.dto.request.PostCreateRequest;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private static final int PAGE_GROUP_SIZE = 5;


    // 전체 게시물 목록 조회
    @Transactional(readOnly = true)
    public PostDto.PostsAndPageResponse<PostDto.ListInfo> findAllPosts(
            Pageable pageable, String searchKeyword, String searchUser
    ) {

        long totalPostCount = postMapper.countPosts(searchKeyword, searchUser);

        if(totalPostCount == 0) {
            return PostDto.PostsAndPageResponse.<PostDto.ListInfo>builder()
                    .postList(List.of())
                    .currentPage(pageable.getPageNumber() + 1)
                    .currentSize(0)
                    .postPerPage(pageable.getPageSize())
                    .totalPostsCount(0L)
                    .totalPages(0)
                    .pageGroupSize(PAGE_GROUP_SIZE)
                    .build();
        }

        int totalPages = (int) ((totalPostCount + pageable.getPageSize() - 1) / pageable.getPageSize());
        if(pageable.getPageNumber() >= totalPages) {
            throw new CustomException(ErrorCode.PAGE_NOT_FOUND);
        }

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<PostDto.ListInfo> postList =
                postMapper.selectAllPosts(offset, pageSize, searchKeyword, searchUser);

        Page<PostDto.ListInfo> postPage = new PageImpl<>(postList, pageable, totalPostCount);

        return PostDto.PostsAndPageResponse.<PostDto.ListInfo>builder()
                .postList(postPage.getContent())
                .currentPage(postPage.getNumber() + 1)
                .currentSize(postPage.getNumberOfElements())
                .postPerPage(postPage.getSize())
                .totalPostsCount(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .pageGroupSize(PAGE_GROUP_SIZE)
                .build();
    }
    
    @Transactional
    public void savePost(PostCreateRequest postCreateRequest) {
        // userId 검증은 나중에 인증 구현 후 추가 예정
        postMapper.save(Post.from(postCreateRequest));
    }
    
    @Transactional(readOnly = true)
    public PostDto.PostResponse findPostById(Long postId) {

        PostDto.PostResponse post = postMapper.selectPostById(postId).
                orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostDto.PostResponse.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .build();
    }

    @Async
    @Transactional
    public void incrementViewCountAsync(Long postId) {
        postMapper.updateViewCount(postId);
    }
    
    @Transactional
    public void updatePost(Long postId, PostDto.UpdateRequest request) {
        boolean exists = postMapper.existsById(postId);
        if(!exists) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
    
        boolean isAuthor = postMapper.existsByPostIdAndUserId(postId, request.getUserId());
        if(!isAuthor) {
            throw new CustomException(ErrorCode.NO_POST_AUTHORITY);
        }
        postMapper.updatePostById(postId, Post.fromUpdateRequest(request));
    }
  
    @Transactional
    public void deletePost(Long postId, Long userId) {
        boolean exists = postMapper.existsById(postId);
        if(!exists) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        boolean isAuthor = postMapper.existsByPostIdAndUserId(postId, userId);
        if(!isAuthor) {
            throw new CustomException(ErrorCode.NO_POST_AUTHORITY);
        }

        postMapper.deletePostById(postId, userId);
    }

}