package br.com.jeanclaro.gasta_pouco.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jeanclaro.gasta_pouco.providers.JWTUserProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityTransactionFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUserProvider jwtUserProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                if(request.getRequestURI().matches("/swagger-ui.*|/v3/api-docs.*|/swagger-resources.*|/webjars.*|/error")){
                    filterChain.doFilter(request, response);
                    return;
                }

                String header = request.getHeader("Authorization");

                if(request.getRequestURI().startsWith("/transaction")){
                    if(header != null){
                        var token = this.jwtUserProvider.validateToken(header);
                        System.out.println("------------- HEADER ---------------");
                        System.out.println(header);
                        System.out.println("Decoded Token: " + token);
                        if(token == null){
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return;
                        }
                        request.setAttribute("user_id", token.getSubject());
                        var roles = token.getClaim("roles").asList(Object.class);
                        System.out.println("Roles Claim: " + token.getClaim("roles").asList(Object.class));

                        var grants = roles.stream()
                        .map(
                            role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())
                        ).toList();

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(),null,grants);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
                filterChain.doFilter(request, response);
}
}
