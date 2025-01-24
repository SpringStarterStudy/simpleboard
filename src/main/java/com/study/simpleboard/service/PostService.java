package com.study.simpleboard.service;

import com.study.simpleboard.domain.Post;
import com.study.simpleboard.dto.PostCreateReq;
import com.study.simpleboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void savePost(PostCreateReq postCreateReq) {
        // userId 검증은 나중에 인증 구현 후 추가 예정
        postRepository.save(Post.from(postCreateReq));
    }
}
