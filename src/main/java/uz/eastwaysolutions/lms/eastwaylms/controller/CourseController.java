package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Create a new course", description = "Allows an admin to create a new course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admin users can create courses")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/courses/create")
    public ResponseEntity<Long> createCourse(
            @Valid @RequestBody CoursesDto coursesDto
    ) {
        courseService.createCourse(coursesDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Update an existing course", description = "Allows an admin to update an existing course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/courses/update/{id}")
    public ResponseEntity<Long> updateCourse(
            @Parameter(description = "ID of the course to update") @PathVariable Long id,
            @RequestBody UpdateCourseDto updateCourseDto
    ) {
        try {
            Long updatedCourseId = courseService.updateCourse(id, updateCourseDto);
            return ResponseEntity.ok(updatedCourseId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(summary = "Delete a course", description = "Allows an admin to delete a course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/courses/delete/{id}")
    public ResponseEntity<String> deleteCourse(
            @Parameter(description = "ID of the course to delete") @PathVariable Long id
    ) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }


    @Operation(summary = "Get all courses", description = "Retrieves a list of all courses. Accessible to both admin and user roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courses")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/both/courses/getAll")
    public ResponseEntity<List<Courses>> getAllCourses() {
        List<Courses> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }


    @Operation(summary = "Get course by ID", description = "Retrieves a course by its ID. Accessible to both admin and user roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the course"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/both/courses/getByID/{id}")
    public ResponseEntity<Courses> getCourseById(
            @Parameter(description = "ID of the course to retrieve") @PathVariable Long id
    ) {
        Optional<Courses> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
}
