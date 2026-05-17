package io.github.jaemo_lim.mapleplanet_spring.global.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class ApiRateLimitFilter extends OncePerRequestFilter {

    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(20_000)
            .build();

    @Value("${app.rate-limit.general-per-minute:120}")
    private int generalPerMinute;

    @Value("${app.rate-limit.preset-save-per-minute:20}")
    private int presetSavePerMinute;

    @Value("${app.max-request-bytes:65536}")
    private long maxRequestBytes;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getContentLengthLong() > maxRequestBytes) {
            writeError(response, HttpStatus.PAYLOAD_TOO_LARGE, "Request body is too large.");
            return;
        }

        String bucketName = request.getRequestURI().startsWith("/api/v1/preset/save")
                ? "preset-save"
                : "general";
        int capacity = "preset-save".equals(bucketName) ? presetSavePerMinute : generalPerMinute;
        String key = clientIp(request) + ":" + bucketName;

        Bucket bucket = buckets.get(key, ignored -> new Bucket(capacity));
        if (!bucket.tryConsume(capacity)) {
            writeError(response, HttpStatus.TOO_MANY_REQUESTS, "Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"" + message + "\",\"errors\":[]}");
    }

    private static final class Bucket {
        private int tokens;
        private long windowStartMs;

        private Bucket(int capacity) {
            this.tokens = capacity;
            this.windowStartMs = System.currentTimeMillis();
        }

        private synchronized boolean tryConsume(int capacity) {
            long now = System.currentTimeMillis();
            if (now - windowStartMs >= WINDOW.toMillis()) {
                tokens = capacity;
                windowStartMs = now;
            }
            if (tokens <= 0) {
                return false;
            }
            tokens--;
            return true;
        }
    }
}
