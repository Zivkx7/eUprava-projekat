package com.facultyservice.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class InternalKeyFilter extends OncePerRequestFilter {

    @Value("${faculty.internal.key}")
    private String internalKey;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (req.getRequestURI().startsWith("/internal")) {
            String key = req.getHeader("X-Internal-Key");
            if (key == null || !key.equals(internalKey)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}