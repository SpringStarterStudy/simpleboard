package com.study.simpleboard.controller;

import com.study.simpleboard.domain.TargetType;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentReactionController {

    @Autowired
    private CommentReactionService commentReactionService;

    @GetMapping("/main")
    public String mainP(){
        return "main";
    }

    @PostMapping("comments/{commentId}/reaction")
    public String addCommentReaction(@PathVariable Long commentId, @RequestBody CommentReactionDTO commentReactionDTO) {

        CommentReactionDTO reaction = new CommentReactionDTO(
                commentReactionDTO.reactionId(),
                commentReactionDTO.userId(),
                commentId,
                TargetType.COMMENT,
                commentReactionDTO.reactionType(),
                commentReactionDTO.isActive()
        );


        commentReactionService.addCommentReaction(commentId, commentReactionDTO);
        return "Reaction added successfully!";
    }

}
