package com.project.futabuslines.configurations;

import com.project.futabuslines.models.User;
import com.project.futabuslines.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    // User's Detail Object
    @Bean
    public UserDetailsService userDetailsService(){
        return identifier -> {
            Optional<User> optionalUser;
            if (identifier.contains("@")) {
                optionalUser = userRepository.findByEmail(identifier);
            } else {
                optionalUser = userRepository.findByPhoneNumber(identifier);
            }

            return optionalUser.orElseThrow(() ->
                    new UsernameNotFoundException("Không tìm thấy người dùng với thông tin: " + identifier));
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    )throws Exception{
        return config.getAuthenticationManager();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/v1/users/**",
//                                "/api/v1/otp/**",
//                                "/api/v1/ticket/vnpay-return",
//                                "/api/v1/payment/vnpay/notify"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .authenticationProvider(authenticationProvider());
//
//        return http.build();
//    }
}
