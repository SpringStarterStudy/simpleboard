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
    public void updatePost(Long postId, PostDto.UpdateRequest request) {
        log.info("updatePost 서비스 실행");
        log.info("postId = " + String.valueOf(postId) + ", title = " + request.getTitle() + ", content = " + request.getContent());
        boolean exists = postMapper.existsById(postId);
        if(!exists) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        // TODO: 사용자 인증 추가해야 함.



        postMapper.updatePostById(postId, request);
    }

}