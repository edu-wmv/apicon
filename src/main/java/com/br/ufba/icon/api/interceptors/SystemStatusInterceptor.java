package com.br.ufba.icon.api.interceptors;

import com.br.ufba.icon.api.domain.SystemStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SystemStatusInterceptor implements HandlerInterceptor {
    private final SystemStatus systemStatus;

    public SystemStatusInterceptor(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!systemStatus.getStatus()) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getWriter().write("Sistema desativado. Tente novamente mais tarde.");
            return false;
        }
        return true;
    }
}
