package com.example.pamw.security;

import com.example.pamw.entity.User;
import com.example.pamw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Value("${auth0.audience}")
    private String audience;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!(request.getRequestURI().startsWith("/api/auth") || request.getRequestURI().startsWith("/ws")))
        {

            String headerAuth = request.getHeader("Authorization");
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
            {
                try
                {
                    String jwt = headerAuth.substring(7);
                    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                            JwtDecoders.fromOidcIssuerLocation(issuer);

                    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
                    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
                    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

                    jwtDecoder.setJwtValidator(withAudience);

                    Jwt decoded = jwtDecoder.decode(jwt);
                    String username = decoded.getSubject();
                    String email = request.getHeader("email");
                    if (!userRepository.findByUsername(username).isPresent())
                    {
                        userRepository.save(new User(username, email));
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (Exception e)
                {
                    System.err.println("Cannot authenticate token " + e);
                }
            } else if (headerAuth.startsWith("MyBearer "))
            {
                try
                {
                    String jwt = headerAuth.substring(9);
                    if (jwtUtils.validateJwtToken(jwt))
                    {
                        String username = jwtUtils.getUserNameFromJwtToken(jwt);

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e)
                {
                    System.err.println("Cannot authenticate token " + e);
                }
            } else
            {
                throw new RuntimeException("Invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }
}
