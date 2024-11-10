package uz.eastwaysolutions.lms.eastwaylms.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;

import uz.eastwaysolutions.lms.eastwaylms.dto.courses.UpdateCourseDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.service.CourseService;

import java.util.List;
import java.util.Optional;


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


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/courses/update/{id}")
    public ResponseEntity<Long> updateCourse(@PathVariable Long id, @RequestBody UpdateCourseDto updateCourseDto) {
        try {
            Long updatedCourseId = courseService.updateCourse(id, updateCourseDto);
            return new ResponseEntity<>(updatedCourseId, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/courses/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/both/courses/getAll")
    public ResponseEntity<List<Courses>> getAllCourses() {
        List<Courses> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/both/courses/getByID/{id}")
    public ResponseEntity<Courses> getCourseById(@PathVariable Long id) {
        Optional<Courses> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }




}

