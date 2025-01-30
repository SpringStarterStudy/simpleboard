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

@Service
@RequiredArgsConstructor
@Slf4j
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

}
