package dev.mohan.greenthumb.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.Role;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.dto.AuthResponseDTO;
import dev.mohan.greenthumb.dto.LoginRequestDTO;
import dev.mohan.greenthumb.dto.RegisterRequestDTO;
import dev.mohan.greenthumb.dto.UserDTO;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.RoleRepository;
import dev.mohan.greenthumb.repository.UserRepository;
import dev.mohan.greenthumb.security.TokenProvider;
import dev.mohan.greenthumb.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;   // the BCrypt bean from SecurityConfiguration
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserDTO register(RegisterRequestDTO request) {
        // 1. reject duplicate email
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already in use: " + request.email());
        }

        // 2. build the user, HASHING the password (never store it raw)
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));   // ← BCrypt hash
        user.setActive(true);                                            // active immediately (no email verification yet)

        // 3. give them the default role
        Role userRole = roleRepository.findById("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role not found: ROLE_USER"));
        user.setRoles(Set.of(userRole));

        // 4. save and return a safe DTO
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // hands credentials to the supervisor → loads user, checks BCrypt password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        // credentials good → print the badge
        String token = tokenProvider.generateToken(authentication);
        return new AuthResponseDTO(token);
    }

    private UserDTO toDto(User u) {
        Set<String> roleNames = u.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserDTO(u.getId(), u.getName(), u.getEmail(), roleNames);
    }
}