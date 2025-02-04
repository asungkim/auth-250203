package com.example.auth.global;

import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.service.MemberService;
import com.example.auth.domain.post.post.service.PostService;
import com.example.auth.global.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

// Request, Response, Session, Cookie, Header
@Component
@RequiredArgsConstructor
@RequestScope // request 마다 주입
public class Rq {

    private final HttpServletRequest request;
    private final MemberService memberService;

    public Member getAuthenticatedWriter() {
        String authorizationValue = request.getHeader("Authorization");

        String apiKey = authorizationValue.replaceAll("Bearer ","");
        Optional<Member> opWriter = memberService.findByApiKey(apiKey);

        if (opWriter.isEmpty()) {
            throw new ServiceException("401-1", "비밀번호가 일치하지 않습니다.");
        }

        return opWriter.get();
    }
}
