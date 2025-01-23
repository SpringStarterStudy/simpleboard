package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
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