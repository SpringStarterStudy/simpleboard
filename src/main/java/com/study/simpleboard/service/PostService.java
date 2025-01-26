package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.Post;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostMapper postMapper;

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

        postMapper.updatePostById(postId, Post.update(request));
    }

}