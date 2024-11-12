package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdatePasswordRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdateUserInfoRequest;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.service.UserServiceForCRUD;
import uz.eastwaysolutions.lms.eastwaylms.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserServiceForCRUD userServiceForCRUD;

    public UserController(UserService userService, UserServiceForCRUD userServiceForCRUD) {
        this.userService = userService;
        this.userServiceForCRUD = userServiceForCRUD;
    }

    @Operation(summary = "Update User Information", description = "Allows a user to update their personal information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PutMapping("/cabinet/update/user-info")
    public ResponseEntity<Void> updateUserInfo(
            @Parameter(description = "Request body containing updated user information") @Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        userService.updateUserInfo(updateUserInfoRequest);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Update User Password", description = "Allows a user to update their password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password data", content = @Content)
    })
    @PutMapping("/cabinet/update/password")
    public ResponseEntity<Void> updatePassword(
            @Parameter(description = "Request body containing the new password") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(updatePasswordRequest);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Enroll User in Course", description = "Enrolls a user in a specified course. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User enrolled in course successfully"),
            @ApiResponse(responseCode = "404", description = "User or course not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/enrollCourse/{courseId}/{userId}")
    public ResponseEntity<String> enrollInCourse(
            @Parameter(description = "ID of the user to enroll", required = true) @PathVariable Long userId,
            @Parameter(description = "ID of the course to enroll the user in", required = true) @PathVariable Long courseId) {
        userServiceForCRUD.enrollUserInCourse(userId, courseId);
        return ResponseEntity.ok("User enrolled in course successfully");
    }


    @Operation(summary = "Unenroll User from Course", description = "Unenrolls a user from a specified course. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unenrolled from course successfully"),
            @ApiResponse(responseCode = "404", description = "User or course not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/unEnrollCourse/{courseId}/{userId}")
    public ResponseEntity<String> unEnrollFromCourse(
            @Parameter(description = "ID of the user to unenroll", required = true) @PathVariable Long userId,
            @Parameter(description = "ID of the course to unenroll the user from", required = true) @PathVariable Long courseId) {
        userServiceForCRUD.unEnrollUserFromCourse(userId, courseId);
        return ResponseEntity.ok("User unenrolled from course successfully");
    }


    @Operation(summary = "Get User's Courses", description = "Retrieves a list of courses the user is enrolled in. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user's courses"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/courses/{userId}")
    public ResponseEntity<List<Courses>> getUserCourse(
            @Parameter(description = "ID of the user whose courses are to be retrieved", required = true) @PathVariable Long userId) {
        List<Courses> userCourses = userServiceForCRUD.getUserCourses(userId);
        return ResponseEntity.ok(userCourses);
    }
}
