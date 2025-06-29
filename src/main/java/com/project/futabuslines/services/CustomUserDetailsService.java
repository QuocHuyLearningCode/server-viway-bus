package com.project.futabuslines.services;

import com.project.futabuslines.models.User;
import com.project.futabuslines.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> optionalUser;

        if (identifier.contains("@")) {
            optionalUser = userRepository.findByEmail(identifier);
        } else {
            optionalUser = userRepository.findByPhoneNumber(identifier);
        }

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Người dùng không tồn tại với thông tin đăng nhập: " + identifier);
        }

        return optionalUser.get(); // Giả sử User implements UserDetails
    }
}

