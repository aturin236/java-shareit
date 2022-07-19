package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static Comment toComment(CommentDtoIn commentDtoIn) {
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());

        return comment;
    }

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return CommentDtoOut.builder()
                .Id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .author(comment.getAuthor())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDtoOut> toCommentDtoOut(Iterable<Comment> comments) {
        List<CommentDtoOut> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(toCommentDtoOut(comment));
        }
        return dtos;
    }
}
