package uz.eastwaysolutions.lms.eastwaylms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;

@Service
public class SessionService {
    public User getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
