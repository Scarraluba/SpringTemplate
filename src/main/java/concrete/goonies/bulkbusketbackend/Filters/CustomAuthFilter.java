package concrete.goonies.bulkbusketbackend.Filters;

import concrete.goonies.bulkbusketbackend.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthFilter extends OncePerRequestFilter {

    protected static final String EMAIL = "email";
    protected static final String TOKEN = "token";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String[] PUBLIC_ROUTES = {"/user/login", "/user/register", "/user/verify/code"};

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = getToken(request);
            if (token != null) {
                String email = tokenProvider.getSubject(token, request);

                if (email != null && tokenProvider.isTokenValid(email, token)) {
                    List<GrantedAuthority> authorities = tokenProvider.grantedAuthorities(token);
                    Authentication authentication = tokenProvider.getAuthentication(email, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //log.debug("Authenticated request for {}", email);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            //log.error("Authentication filter failed", exception);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String authorization = request.getHeader(AUTHORIZATION);

        return authorization == null ||
                !authorization.startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase("OPTIONS") ||
                Arrays.asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    private String getToken(HttpServletRequest request) {

        String authorization = request.getHeader(AUTHORIZATION);
       //log.info("Authorization header: {}", authorization);
        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        String token = authorization.substring(TOKEN_PREFIX.length()).trim();
        return token;
    }
}