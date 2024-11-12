package uz.eastwaysolutions.lms.eastwaylms.service;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.entity.Role;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;
import uz.eastwaysolutions.lms.eastwaylms.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceForCRUD {

    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;

    public UserServiceForCRUD(UserRepository userRepository, CoursesRepository coursesRepository) {
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
    }

    public void enrollUserInCourse(Long userId, Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can assign courses to users.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        user.addCourse(course);
        userRepository.save(user);
    }

    public void unEnrollUserFromCourse(Long userId, Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can remove courses from users.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        user.removeCourse(course);
        userRepository.save(user);
    }

    public List<Courses> getUserCourses(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getCourses();
    }


}
