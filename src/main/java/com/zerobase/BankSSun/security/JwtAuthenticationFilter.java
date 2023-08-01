package com.zerobase.BankSSun.security;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer "; // 인증 타입

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // 다음 필터 실행 전 처리하는 로직
        String token = resolveTokenFromRequest(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 토큰 유효성 검증
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw new RuntimeException("유효하지 않은 토큰입니다");
        }
        // 다음 filter-chain 실행
        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }


}
