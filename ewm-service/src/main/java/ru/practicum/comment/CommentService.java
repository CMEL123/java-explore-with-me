package ru.practicum.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.EventService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final EventService eventService;
    private final UserService userService;

    private final CommentMapper commentMapper;

    public List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size) {
        eventService.findById(eventId);

        Pageable pageRequest = PageRequest.of(from / size, size);
        Page<Comment> commentPage = commentRepository.findByEventId(eventId, pageRequest);

        return commentPage.getContent()
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .toList();
    }

    public List<CommentDto> getCommentsByAuthorId(Long userId, Integer from, Integer size) {
        userService.findById(userId);

        Pageable pageRequest = PageRequest.of(from / size, size);
        Page<Comment> commentPage = commentRepository.findByEventId(userId, pageRequest);

        return commentPage.getContent()
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .toList();
    }


    public CommentDto createComment(@Valid CommentCreateDto commentDto, Long userId, Long eventId) {
        return commentMapper.toCommentDto(
                commentRepository.save(
                        commentMapper.toComment(
                                commentDto,
                                userService.findById(userId),
                                eventService.findById(eventId)
                        )
                )
        );
    }


    public CommentDto updateComment(@Valid CommentCreateDto commentDto, Long userId, Long commentId) {
        Comment commentOld = findById(commentId);
        checkUserIsAuthor(commentOld, userId);
        commentOld.setText(commentDto.getText());
        commentOld.setUpdated(LocalDateTime.now());
        return commentMapper.toCommentDto(
                commentRepository.save(commentOld)
        );
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findById(commentId);
        checkUserIsAuthor(comment, userId);
        commentRepository.delete(comment);
    }

    public CommentDto getCommentById(Long commentId) {
        return commentMapper.toCommentDto(findById(commentId));
    }

    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
    }

    private void checkUserIsAuthor(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ValidationException(
                    String.format(
                            "User with id=%d was not found is not author of comment with id=%d",
                            userId,
                            comment.getId()
                    )
            );
        }
    }

    public Comment findById(Long id) {
        Optional<Comment> comment =  commentRepository.findById(id);
        if  (comment.isPresent()) {
            log.info("Комментарий c id = {} найден", id);
            return comment.get();
        }

        log.warn("Комментарий с id = {} не найден", id);
        throw new NotFoundException(String.format("Comment with id=%d was not found", id));
    }

}
