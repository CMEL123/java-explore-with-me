package ru.practicum.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface CommentMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventTitle", source = "event.title")
    @Mapping(target = "eventDescription", source = "event.description")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updated", expression = "java(LocalDateTime.now())")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "event", source = "event")
    Comment toComment(CommentCreateDto dto, User author, Event event);
}