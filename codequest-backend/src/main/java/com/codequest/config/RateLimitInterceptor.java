package com.codequest.config;

import com.codequest.entity.AppSettings;
import com.codequest.repository.AppSettingsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final AppSettingsRepository appSettingsRepository;

    private final ConcurrentHashMap<String, Long> lastSubmitMap = new ConcurrentHashMap<>();

    private volatile int cachedRateLimit = 1;
    private volatile long cacheLoadedAt = 0;

    private int getRateLimit() {
        long now = System.currentTimeMillis();
        if (now - cacheLoadedAt > 60_000) {
            AppSettings settings = appSettingsRepository.findAll().stream().findFirst().orElse(null);
            if (settings != null) {
                cachedRateLimit = settings.getSandboxRateLimit();
            }
            cacheLoadedAt = now;
        }
        return cachedRateLimit;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (!"POST".equalsIgnoreCase(method)) return true;
        if (!uri.contains("/challenges/")) return true;
        String type = "";
        if (uri.endsWith("/run")) type = "_run";
        else if (uri.endsWith("/submit")) type = "_submit";
        else return true;

        var authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return true;

        Long userId;
        try {
            userId = (Long) authentication.getPrincipal();
        } catch (ClassCastException e) {
            return true;
        }

        int rateLimit = getRateLimit(); 
        
        long windowMs = (rateLimit > 0) ? (10_000L / rateLimit) : 10_000L;

        long now = System.currentTimeMillis();
        String key = userId + type;
        Long lastSubmit = lastSubmitMap.get(key);

        if (lastSubmit != null && (now - lastSubmit) < windowMs) {
            long waitMs = windowMs - (now - lastSubmit);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                String.format("{\"error\": \"Rate limit atteint. Veuillez attendre %d seconde(s) avant de réessayer.\", \"waitMs\": %d}",
                    (waitMs / 1000) + 1, waitMs)
            );
            return false;
        }

        lastSubmitMap.put(key, now);
        return true;
    }
}
