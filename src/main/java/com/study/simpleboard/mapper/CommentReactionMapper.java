package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.CommentReactionDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentReactionMapper {
    void insertCommentReaction(CommentReactionDTO commentReactionDTO);
}