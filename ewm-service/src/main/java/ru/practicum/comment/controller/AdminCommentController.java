package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getCommentByIdByAdmin(
            @PathVariable Long commentId
    ) {
        return commentService.getCommentByIdByAdmin(commentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentCreateDto commentCreateDto
    ) {
        return commentService.updateCommentByAdmin(commentCreateDto, commentId);
    }
}