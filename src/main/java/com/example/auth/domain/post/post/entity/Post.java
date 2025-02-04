package com.example.auth.domain.post.post.entity;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.post.comment.entity.Comment;
import com.example.auth.global.entity.BaseTime;
import com.example.auth.global.exception.ServiceException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private String title;
    private String content;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public Comment addComment(Member author, String content) {
        Comment comment = Comment.builder()
                .post(this)
                .author(author)
                .content(content)
                .build();

        this.comments.add(comment);

        return comment;
    }

    public Comment getCommentById(long commentId) {
        return comments.stream()
                .filter(c -> c.getId() == commentId)
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException("404-2", "해당 댓글은 존재하지 않습니다.")
                );
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
    }
}
