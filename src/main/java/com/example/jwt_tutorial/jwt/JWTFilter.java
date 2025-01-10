package com.example.jwt_tutorial.jwt;

import com.example.jwt_tutorial.dto.CustomUserDetails;
import com.example.jwt_tutorial.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            log.debug("token null");
            filterChain.doFilter(request,response);
            return; // 조건에 해당되면 메서드 종료
        }

        log.debug("authorization now");
        String token = authorization.split(" ")[1];

        if(jwtUtil.isExpired(token)) {
            log.debug("token expired");
            filterChain.doFilter(request,response);
            return; // 조건에 해당되면 메서드 종료
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("temppassword");
        userEntity.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request,response);

    }
}
