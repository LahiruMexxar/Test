package com.Mexxar.Test.Service;

import com.Mexxar.Test.DTO.AuthResponse;
import com.Mexxar.Test.Model.AuthRequest;
import com.Mexxar.Test.Model.RegRequest;
import com.Mexxar.Test.Model.User;
import com.Mexxar.Test.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegRequest request) {
        try {
            // Check if the email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DataIntegrityViolationException("Email address already in use. Please use a different email.");
            }

            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();

            // Save the user to the database
            userRepository.save(user);

            // Generate JWT token
            var jwtToken = jwtService.generateToken(user);

            // Return a meaningful message indicating successful user creation
            return AuthResponse.builder()
                    .token(jwtToken)
                    .message("User successfully registered.")
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Handle data duplication exception
            return AuthResponse.builder()
                    .message("Error: " + e.getMessage())
                    .build();
        }
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            // Authentication failed
            return AuthResponse.builder()
                    .message("Authentication failed. Invalid credentials.")
                    .build();
        }

        // Authentication successful
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // You might want to handle the case where the user is not found
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .message("Authentication successful.")
                .build();
    }

}
