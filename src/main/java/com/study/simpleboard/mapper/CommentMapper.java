package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.response.CommentResponseDTO;
import java.util.List;

import com.study.simpleboard.dto.ReplyCreateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    boolean existsByCommentId(Long commentId);

    Long findPostIdByCommentId(Long commentId);

    void insertComment(CommentCreateDTO createDTO);

    void insertReply(ReplyCreateDTO replyDTO);

    List<CommentResponseDTO> selectCommentList(Long postId);

    boolean checkUser(Long userId, Long commentId);

    boolean checkCommentId(Long commentId);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, String commentContent);

}
