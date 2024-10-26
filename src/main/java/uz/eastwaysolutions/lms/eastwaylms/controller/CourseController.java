package uz.eastwaysolutions.lms.eastwaylms.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;

import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.service.CourseService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/admin/courses/getAll")
    public ResponseEntity<List<Courses>> getAllCourses() {
        List<Courses> coursesList = courseService.getAllCourses();
        return ResponseEntity.ok(coursesList);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/courses/add")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CoursesDto coursesDto) {
        String result = courseService.addCourse(coursesDto);

        if (result.equals("Course added successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/courses/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Long id, @Valid @RequestBody CoursesDto coursesDto) {
        String result = courseService.updateCourse(id, coursesDto);

        if (result.equals("Course updated successfully")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("Course not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        String result = courseService.deleteCourse(id);

        if (result.equals("Course deleted successfully")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("Course not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
    }
}

