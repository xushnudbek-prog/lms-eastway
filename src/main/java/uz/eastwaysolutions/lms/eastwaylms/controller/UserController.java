package uz.eastwaysolutions.lms.eastwaylms.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdatePasswordRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdateUserInfoRequest;
import uz.eastwaysolutions.lms.eastwaylms.service.user.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
