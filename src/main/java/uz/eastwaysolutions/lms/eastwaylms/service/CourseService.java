package uz.eastwaysolutions.lms.eastwaylms.service;


import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.entity.Role;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;

import java.util.List;

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
            courses.setCreatedBy(user.getId());
            coursesRepository.save(courses);
        }else{
            return "User can not create Course";
        }
        return "Course added successfully";

    }
}
