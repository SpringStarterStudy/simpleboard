package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostMapper postMapper;

    // 전체 게시물 목록 조회
    @Transactional(readOnly = true)
    public PostDto.PostsAndPageResponse<PostDto.Info> findAllPost(
            Pageable pageable, String searchKeyword, String searchUser
    ) {

        long totalPostCount = postMapper.countPosts(searchKeyword, searchUser);

        if(totalPostCount == 0) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        int totalPages = (int) ((totalPostCount + pageable.getPageSize() - 1) / pageable.getPageSize());
        if(pageable.getPageNumber() >= totalPages) {
            throw new CustomException(ErrorCode.PAGE_NOT_FOUND);
        }

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<PostDto.Info> postList = Optional.ofNullable(
                postMapper.selectAllPost(offset, pageSize, searchKeyword, searchUser))
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Page<PostDto.Info> postPage = new PageImpl<>(postList, pageable, totalPostCount);

        return PostDto.PostsAndPageResponse.<PostDto.Info>builder()
                .content(postPage.getContent())
                .currentPage(postPage.getNumber() + 1)
                .currentSize(postPage.getNumberOfElements())
                .postPerPage(postPage.getSize())
                .totalPostsCount(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .pageGroupSize(5)
                .build();
    }

}
