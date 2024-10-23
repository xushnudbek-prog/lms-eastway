package uz.eastwaysolutions.lms.eastwaylms.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.AuthRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.AuthResponse;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.RegistrationRequest;
import uz.eastwaysolutions.lms.eastwaylms.entity.Role;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.repository.UserRepository;
import uz.eastwaysolutions.lms.eastwaylms.security.JwtService;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest registrationRequest) {
        userRepository.findByPhoneNumber(registrationRequest.getPhoneNumber())
                .ifPresent(user -> {
                    throw new EntityExistsException("Phone number already exists");
                });
        User user = User.builder()
                .phoneNumber(registrationRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .role(Role.USER)
                .lastLoginDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var authenticatedUser = ((User) auth.getPrincipal());
        claims.put("fullName", authenticatedUser.getFullName());

        var accessToken = jwtService.generateToken(claims, authenticatedUser);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
