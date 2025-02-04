package com.example.auth.domain.post.comment.entity;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.post.post.entity.Post;
import com.example.auth.global.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseTime {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    public void modify(String content) {
        this.content = content;
    }
}
