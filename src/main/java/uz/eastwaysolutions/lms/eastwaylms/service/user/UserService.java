package uz.eastwaysolutions.lms.eastwaylms.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdatePasswordRequest;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UpdateUserInfoRequest;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.repository.UserRepository;
import uz.eastwaysolutions.lms.eastwaylms.service.SessionService;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;


    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        User user = sessionService.getSession();
        user.setFirstName(updateUserInfoRequest.getFirstName());
        user.setLastName(updateUserInfoRequest.getLastName());
        userRepository.save(user);
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = sessionService.getSession();
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
        userRepository.save(user);
    }
}
