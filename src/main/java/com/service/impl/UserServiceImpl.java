package com.service.impl;

import com.domain.User;
import com.repository.UserRepository;
import com.service.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

//    @Override
//    public UserDto registerUser(UserCreateDto userCreateDto) {
//        // Check if passwords match
//        if (!userCreateDto.getPassword().equals(userCreateDto.getConfirmPassword())) {
//            throw new IllegalArgumentException("Passwords do not match");
//        }
//
//        // Check if the username is already taken
//        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
//            throw new IllegalArgumentException("Username is already taken");
//        }
//
//        // Create and save the user
//        User user = new User();
//        user.setUsername(userCreateDto.getUsername());
//        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword())); // Password is hashed
//        return userMapper.toDto(userRepository.save(user));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}

//    @Override
//    public Optional<User>  findByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username);
//    }

