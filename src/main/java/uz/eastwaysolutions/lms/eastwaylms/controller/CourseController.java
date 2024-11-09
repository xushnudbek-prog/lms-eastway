package uz.eastwaysolutions.lms.eastwaylms.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;

import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;
import uz.eastwaysolutions.lms.eastwaylms.service.CourseService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/courses/create")
    public ResponseEntity<Long> createCourse(@Valid @RequestBody CoursesDto coursesDto) {
        courseService.createCourse(coursesDto);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}

