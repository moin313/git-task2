package com.spring.security.filter;

import com.spring.security.util.Jwtutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class securityFilter extends OncePerRequestFilter {

    @Autowired
    private Jwtutil util;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null){
            String username = util.getUsername(token);
            if(username != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null){

                UserDetails user = userDetailsService.loadUserByUsername(username);

                if(util.validateToken(token, user.getUsername())){

                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    //FINAL OBJECT STORES IN SECURITY-CONTEXT WITH USER DETAILS
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }
        }
        filterChain.doFilter(request, response);
    }
}