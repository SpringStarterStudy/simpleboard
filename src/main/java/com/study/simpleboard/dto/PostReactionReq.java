package com.study.simpleboard.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PostReactionReq {
    private final Long userId;
    private final Boolean like;
    private final Boolean dislike;
}
