package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.CommentCreateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    void insertComment(CommentCreateDTO createDTO);
}
