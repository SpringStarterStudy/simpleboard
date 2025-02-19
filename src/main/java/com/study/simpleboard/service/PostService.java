package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.request.PostRequestDTO;
import com.study.simpleboard.dto.response.PostResponseDTO;
import com.study.simpleboard.domain.Post;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return PostResponseDTO.PostsAndPageResponse.empty(pageable, PAGE_GROUP_SIZE);
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

        return PostResponseDTO.PostsAndPageResponse.of(postPage, PAGE_GROUP_SIZE);
    }

    @Transactional
    public void savePost(PostRequestDTO.CreateAndUpdate request, Long userId) {
        postMapper.save(Post.from(request, userId));
    }

    @Transactional
    public PostResponseDTO.PostDetail findPostById(Long postId) {
        PostResponseDTO.PostDetail post =
                postMapper.selectPostById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postMapper.updateViewCount(postId);
        return post;
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