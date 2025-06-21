package dev.mayutama.smartbook.config;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            if (!"anonymousUser".equals(username)) {
                return Optional.of(username);
            }
        }
        return Optional.of("system");
    }
}