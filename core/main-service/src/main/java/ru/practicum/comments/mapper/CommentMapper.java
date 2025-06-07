package ru.practicum.comments.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentEconomDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.users.model.User;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final EventMapper eventMapper;

    public CommentOutputDto commentToOutputDto(Comment comment) {
        User user = User.builder()
                .id(comment.getUser().getId())
                .email(comment.getUser().getEmail())
                .name(comment.getUser().getName())
                .build();

        return CommentOutputDto.builder()
                .id(comment.getId())
                .user(user)
                .event(eventMapper.toEventShortDto(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }

    public CommentEconomDto commentToEconomDto(Comment comment) {
        return CommentEconomDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }
}
