package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
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
    public PostDto.Detail findPostById(Long postId) {
        if(postId <= 0) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        PostDto.Detail post = postMapper.selectPostById(postId).
                orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postMapper.updateViewCount(post.getId());

        return PostDto.Detail.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount() + 1)
                .build();
    }

}