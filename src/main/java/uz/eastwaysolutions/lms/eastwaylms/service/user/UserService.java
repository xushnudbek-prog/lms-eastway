package uz.eastwaysolutions.lms.eastwaylms.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdatePasswordRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdateUserInfoRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UserProfileDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.exception.InvalidPasswordException;
import uz.eastwaysolutions.lms.eastwaylms.repository.UserRepository;
import uz.eastwaysolutions.lms.eastwaylms.service.SessionService;
import uz.eastwaysolutions.lms.eastwaylms.utils.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        User user = sessionService.getSession();
        user.setFirstName(updateUserInfoRequest.getFirstName());
        user.setLastName(updateUserInfoRequest.getLastName());
        userRepository.save(user);
    }

    //TODO Check if the password validation is working correct or not
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = sessionService.getSession();
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password does not match");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
        userRepository.save(user);
    }

    public UserProfileDto fetchUserProfile() {
        User user = sessionService.getSession();
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return userMapper.toUserProfileResponseDto(user);
    }

    public Page<UserProfileDto> getUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageRequest);
        return users.map(userMapper::toUserProfileResponseDto);
    }

    public void updateUserProfile(UserProfileDto userProfileDto) {
        User user = userRepository.findById(userProfileDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setRole(userProfileDto.getRole());
        userRepository.save(user);
    }

    public void deleteUSer(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
