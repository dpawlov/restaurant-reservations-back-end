package com.web;

import com.domain.Role;
import com.domain.User;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.service.dto.UserCreateDto;
import com.service.dto.UserDto;
import com.service.dto.UserLoginDto;
import com.service.mapper.UserMapper;
import com.web.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserCreateDto userCreateDto) {
        try {
            LOGGER.info("Received registration request for username: {}", userCreateDto.getUsername());

            if (userRepository.findByUsername(userCreateDto.getUsername()) != null) {
                LOGGER.error("Username already taken: {}", userCreateDto.getUsername());
                throw new BadRequestException("Username already taken!");
            }

            User user = new User();
            user.setUsername(userCreateDto.getUsername());
            user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
            Role roles = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Default role not found!"));
            user.setRoles(Collections.singleton(roles));
            userRepository.save(user);

            LOGGER.info("User registered successfully: {}", user.getUsername());
            return userMapper.toDto(user);
        } catch (Exception e) {
            LOGGER.error("Error registering user", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public UserDto authenticateUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            LOGGER.info("Received login request for username: {}", userLoginDto.getUsername());

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

            String username = authenticatedUser.getName();
            LOGGER.info("User authenticated successfully: {}", username);

            User byUsername = userRepository.findByUsername(username);

            return userMapper.toDto(byUsername);
        } catch (Exception e) {
            LOGGER.error("Error authenticating user", e);
            throw e;
        }
    }
}

