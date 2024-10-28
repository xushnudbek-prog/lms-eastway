package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.AuthRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.AuthResponse;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.RegistrationRequest;
import uz.eastwaysolutions.lms.eastwaylms.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs")
public class AuthController {

    private final AuthService service;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        service.register(request);
        return ResponseEntity.accepted().build();
    }
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
