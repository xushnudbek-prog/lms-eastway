package uz.eastwaysolutions.lms.eastwaylms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UserProfileDto;
import uz.eastwaysolutions.lms.eastwaylms.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPanelController {
    private final UserService userService;

    @GetMapping("/users")
    public Page<UserProfileDto> getUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return userService.getUsers(page, size);
    }
    @PutMapping("/users/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfile(userProfileDto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUSer(id);
        return ResponseEntity.noContent().build();
    }
}