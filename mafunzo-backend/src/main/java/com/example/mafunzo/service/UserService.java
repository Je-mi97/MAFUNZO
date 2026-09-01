package com.example.mafunzo.service;

import com.example.mafunzo.dto.LoginRequest;
import com.example.mafunzo.dto.LoginResponse;
import com.example.mafunzo.dto.RegisterRequest;
import com.example.mafunzo.dto.UserResponse;
import com.example.mafunzo.model.User;
import com.example.mafunzo.repository.UserRepository;
import com.example.mafunzo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Cette adresse email est déjà utilisée."
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole("STUDENT");

        user.setObjective(request.getObjective());
        user.setInterests(request.getInterests());
        user.setLevel(request.getLevel());
        user.setPace(request.getPace());

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Email ou mot de passe incorrect."
                        )
                );

        boolean passwordCorrect =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordCorrect) {

            throw new IllegalArgumentException(
                    "Email ou mot de passe incorrect."
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        UserResponse userResponse =
                new UserResponse(user);

        return new LoginResponse(
                token,
                userResponse
        );
    }
}