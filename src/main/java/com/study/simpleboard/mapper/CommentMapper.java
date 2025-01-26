package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.CommentResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    void insertComment(CommentCreateDTO createDTO);

    List<CommentResponseDTO> selectCommentList(Long postId);

    int checkUser(Long userId, Long commentId);

    int checkCommentId(Long commentId);

    void deleteComment(Long commentId);

}
