package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.lesson.LessonDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Lesson;
import uz.eastwaysolutions.lms.eastwaylms.service.LessonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary = "Create a new lesson", description = "Creates a new lesson for a specified module.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admin users can create lessons")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createLesson/{moduleId}")
    public ResponseEntity<Lesson> createLesson(
            @Parameter(description = "ID of the module to which the lesson belongs", required = true) @PathVariable Long moduleId,
            @RequestBody LessonDto lessonDto) {
        Lesson createdLesson = lessonService.createLesson(moduleId, lessonDto);
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }


    @Operation(summary = "Get all lessons", description = "Retrieves a list of all lessons. Accessible by admin and user roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of lessons")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAllLessons")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }


    @Operation(summary = "Get lesson by ID", description = "Retrieves a lesson by its ID. Accessible by admin and user roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the lesson"),
            @ApiResponse(responseCode = "404", description = "Lesson not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getLesson/{lessonId}")
    public ResponseEntity<Lesson> getLessonByID(
            @Parameter(description = "ID of the lesson to retrieve", required = true) @PathVariable Long lessonId) {
        Optional<Lesson> lesson = lessonService.getLessonByID(lessonId);
        return lesson.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
    }


    @Operation(summary = "Update a lesson", description = "Updates an existing lesson. Only accessible by admin users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully"),
            @ApiResponse(responseCode = "404", description = "Lesson not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateLesson/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @Parameter(description = "ID of the lesson to update", required = true) @PathVariable Long lessonId,
            @Parameter(description = "ID of the module to which the lesson belongs (optional)") @RequestParam(required = false) Long moduleId,
            @RequestBody LessonDto lessonDto) {

        Lesson updatedLesson = lessonService.updateLesson(moduleId, lessonId, lessonDto);
        return ResponseEntity.ok(updatedLesson);
    }


    @Operation(summary = "Delete a lesson", description = "Deletes a lesson by its ID. Only accessible by admin users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lesson deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Lesson not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteLesson/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID of the lesson to delete", required = true) @PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}
