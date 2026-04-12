package com.devestudo.projeto_financas.security;

import com.devestudo.projeto_financas.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new RuntimeException("Usuário não autenticado");
        }

        return user.getId();
    }
}
