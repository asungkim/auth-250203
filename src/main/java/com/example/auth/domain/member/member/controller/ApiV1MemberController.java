package com.example.auth.domain.member.member.controller;

import com.example.auth.domain.member.member.dto.MemberDto;
import com.example.auth.domain.member.member.entity.Member;
import com.example.auth.domain.member.member.service.MemberService;
import com.example.auth.global.Rq;
import com.example.auth.global.dto.RsData;
import com.example.auth.global.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {

    private final MemberService memberService;
    private final Rq rq;

    record JoinReqBody(@NotBlank @Length(min = 3) String username,
                       @NotBlank @Length(min = 3) String password,
                       @NotBlank @Length(min = 3) String nickname) {
    }

    @PostMapping("/join")
    public RsData<MemberDto> join(@RequestBody @Valid JoinReqBody body) {


        memberService.findByUsername(body.username())
                .ifPresent(member -> {
                    throw new ServiceException("400-1", "중복된 아이디입니다.");
                });


        Member member = memberService.join(body.username(), body.password(), body.nickname());

        return new RsData<>(
                "201-1",
                "회원 가입이 완료되었습니다.",
                new MemberDto(member)
        );
    }

    record LoginReqBody(@NotBlank @Length(min = 3) String username,
                        @NotBlank @Length(min = 3) String password) {}

    record LoginResBody(MemberDto memberDto,String apiKey) {}

    @PostMapping("/login")
    public RsData<LoginResBody> login(@RequestBody @Valid LoginReqBody body) {

        Member writer = memberService.findByUsername(body.username())
                .orElseThrow(() -> new ServiceException("400-2", "아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!writer.getPassword().equals(body.password)) {
            throw new ServiceException("401-2","비밀번호가 일치하지 않습니다.");
        }

        return new RsData<>(
                "201-1",
                "%s님 환영합니다.".formatted(writer.getNickname()),
                new LoginResBody(new MemberDto(writer), writer.getApiKey())
        );
    }

    @GetMapping("/me")
    public RsData<MemberDto> me() {
        Member writer = rq.getAuthenticatedWriter();

        return new RsData<>(
                "200-1",
                "내 정보 조회가 완료되었습니다.",
                new MemberDto(writer)
        );
    }

}
