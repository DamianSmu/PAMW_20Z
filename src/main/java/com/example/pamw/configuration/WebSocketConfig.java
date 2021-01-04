package com.example.pamw.configuration;

import com.example.pamw.security.AudienceValidator;
import com.example.pamw.security.JwtUtils;
import com.example.pamw.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils;
    @Value("${auth0.audience}")
    private String audience;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");

        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand()))
                {
                    String headerAuth = accessor.getFirstNativeHeader("Authorization");
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
//                            String email = request.getHeader("email");
//                            if (!userRepository.findByUsername(username).isPresent()) {
//                                userRepository.save(new User(username, email));
//                            }
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                            accessor.setUser(authentication);
                        } catch (Exception e)
                        {
                            System.err.println("Cannot authenticate token " + e);
                        }
                    } else if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("MyBearer "))
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
                                accessor.setUser(authentication);
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
                return message;
            }
        });
    }
}

