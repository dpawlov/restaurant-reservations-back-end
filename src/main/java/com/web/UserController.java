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
        // checking for username exists in a database
        if (userRepository.findByUsername(userCreateDto.getUsername()) != null) {
            throw new BadRequestException("Username already taken!");
        }
        // creating user object
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public UserDto authenticateUser(@RequestBody UserLoginDto userLoginDto) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication getAuthenticatedUser = SecurityContextHolder.getContext().getAuthentication();

        String username = getAuthenticatedUser.getName();

        User byUsername = userRepository.findByUsername(username);

        return userMapper.toDto(byUsername);
    }
}

