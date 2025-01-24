package com.study.simpleboard.repository;

import com.study.simpleboard.domain.Post;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final PostMapper postMapper;

    public void save(Post post) {
        postMapper.save(post);
    }
}
