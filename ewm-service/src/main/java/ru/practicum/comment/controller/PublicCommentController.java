package ru.practicum.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentService;
import ru.practicum.comment.dto.CommentDto;

import java.util.List;

@RestController
@RequestMapping("events/{eventId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping()
    public List<CommentDto> getCommentsByEventId(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return commentService.getCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(
            @PathVariable Long eventId,
            @PathVariable Long commentId
    ) {
        return commentService.getCommentById(eventId, commentId);
    }

}