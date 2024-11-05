package uz.eastwaysolutions.lms.eastwaylms.controller;

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

    @PutMapping("/cabinet/update/user-info")
    public ResponseEntity<Void> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        userService.updateUserInfo(updateUserInfoRequest);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/cabinet/update/password")
    public ResponseEntity<Void> updatePassword(@Valid UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(updatePasswordRequest);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/enrollCourse/{courseId}/{userId}")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userServiceForCRUD.enrollUserInCourse(userId, courseId);
        return ResponseEntity.ok("User enrolled in course successfully");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/unEnrollCourse/{courseId}/{userId}")
    public ResponseEntity<?> unEnrollFromCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userServiceForCRUD.unEnrollUserFromCourse(userId, courseId);
        return ResponseEntity.ok("User unEnrolled from course successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/courses/{userId}")
    public ResponseEntity<List<Courses>> getUserCourse(@PathVariable Long userId){
        List<Courses> userCourses = userServiceForCRUD.getUserCourses(userId);
        return ResponseEntity.ok(userCourses);
    }
}
