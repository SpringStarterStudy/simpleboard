package com.study.simpleboard.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostReactionReq {
    private final Long userId;
    private final Boolean like;
    private final Boolean dislike;
}
