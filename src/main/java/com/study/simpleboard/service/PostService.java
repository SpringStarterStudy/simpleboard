package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.request.PostRequestDTO;
import com.study.simpleboard.dto.response.PostResponseDTO;
import com.study.simpleboard.domain.Post;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private static final int PAGE_GROUP_SIZE = 5;

    @Transactional(readOnly = true)
    public PostResponseDTO.PostsAndPageResponse<PostResponseDTO.PostList> findAllPosts(
            Pageable pageable, String searchKeyword, String searchUser
    ) {

        long totalPostCount = postMapper.countPosts(searchKeyword, searchUser);

        if(totalPostCount == 0) {
            return PostResponseDTO.PostsAndPageResponse.<PostResponseDTO.PostList>builder()
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

        List<PostResponseDTO.PostList> postList =
                postMapper.selectAllPosts(offset, pageSize, searchKeyword, searchUser);

        Page<PostResponseDTO.PostList> postPage = new PageImpl<>(postList, pageable, totalPostCount);

        return PostResponseDTO.PostsAndPageResponse.<PostResponseDTO.PostList>builder()
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
    @PreAuthorize("isAuthenticated() and authentication.principal.userId == #userId")
    public void savePost(PostRequestDTO.CreateAndUpdate request, Long userId) {
        postMapper.save(Post.from(request, userId));
    }

    @Transactional(readOnly = true)
    public PostResponseDTO.PostDetail findPostById(Long postId) {
        PostResponseDTO.PostDetail post =
                postMapper.selectPostById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return post;
    }

    @Transactional
    @Async
    public void incrementViewCountAsync(Long postId) {
        postMapper.updateViewCount(postId);
    }

    @Transactional
    public void updatePost(Long postId, Long userId, PostRequestDTO.CreateAndUpdate request) {
        validatePostOwnerShip(postId, userId);
        postMapper.updatePostById(postId, Post.from(request, userId));
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        validatePostOwnerShip(postId, userId);
        postMapper.deletePostById(postId);
    }

    private void validatePostOwnerShip(Long postId, Long userId) {
        Long authorId = postMapper.findPostAuthor(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!authorId.equals(userId)) {
            throw new CustomException(ErrorCode.NO_POST_AUTHORITY);
        }
    }
}