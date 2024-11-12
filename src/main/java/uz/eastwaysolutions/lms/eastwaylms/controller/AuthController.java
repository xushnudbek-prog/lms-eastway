package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Authentication APIs", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided details",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
            }
    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest request) {
        service.register(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and returns a JWT token upon successful login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
