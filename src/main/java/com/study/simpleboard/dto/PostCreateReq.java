package com.study.simpleboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateReq {
    private final Long userId;
    private final String title;
    private final String content;
}
