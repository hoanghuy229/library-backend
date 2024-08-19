package com.huy2209.library_backend.component;

import com.huy2209.library_backend.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try{
            if(isByPassToken(request)){
                filterChain.doFilter(request,response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String email = jwtUtil.extractEmail(token);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = (User) userDetailsService.loadUserByUsername(email);
                if(jwtUtil.validToken(token,user)){
                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }
        catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"unAuthorized");
        }
    }

    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("api/books", "GET"),
                Pair.of("api/reviews", "GET"),
                Pair.of("api/users", "GET")
        );

        String servletPath = request.getServletPath();
        String method = request.getMethod();

        if (servletPath.contains("api/reviews/by-user") || servletPath.contains("api/users/current-loans") ||
            servletPath.contains("api/users/user-histories") || servletPath.contains("api/messages")) {
            return false;
        }

        for (Pair<String, String> bypassToken : bypassTokens) {
            if (servletPath.contains(bypassToken.getFirst()) && method.equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }

}
