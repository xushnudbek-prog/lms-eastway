package uz.eastwaysolutions.lms.eastwaylms.service;


import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.entity.Role;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CoursesRepository coursesRepository;

    public CourseService( CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }



    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }
    @Transactional
    public String addCourse(CoursesDto coursesDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Courses courses = new Courses();
        if(user.getRole().equals(Role.ADMIN)){
            courses.setTitle(coursesDto.getTitle());
            courses.setDescription(coursesDto.getDescription());
            courses.setMediaUrl(coursesDto.getMediaUrl());
            courses.setCreatedBy(user.getId());
            coursesRepository.save(courses);
        }else{
            return "User can not create Course";
        }
        return "Course added successfully";
    }
    @Transactional
    public String updateCourse(Long courseId, CoursesDto coursesDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (!user.getRole().equals(Role.ADMIN)) {
            return "User is not authorized to update Course";
        }

        Optional<Courses> existingCourse = coursesRepository.findById(courseId);
        if (existingCourse.isPresent()) {
            Courses courseToUpdate = existingCourse.get();
            courseToUpdate.setTitle(coursesDto.getTitle());
            courseToUpdate.setDescription(coursesDto.getDescription());
            courseToUpdate.setMediaUrl(coursesDto.getMediaUrl());
            coursesRepository.save(courseToUpdate);
            return "Course updated successfully";
        } else {
            return "Course not found";
        }
    }

    @Transactional
    public String deleteCourse(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (!user.getRole().equals(Role.ADMIN)) {
            return "User is not authorized to delete Course";
        }

        if (coursesRepository.existsById(courseId)) {
            coursesRepository.deleteById(courseId);
            return "Course deleted successfully";
        } else {
            return "Course not found";
        }
    }

    public List<User> getCourseUsers(Long courseId){
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courses.getUsers();
    }
}
