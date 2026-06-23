package com.re.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ForbiddenEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(@NonNull HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) {
        log.error("Responding with forbidden error. Message - {}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
