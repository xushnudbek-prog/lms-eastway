package uz.eastwaysolutions.lms.eastwaylms.controller;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createLesson/{moduleId}")
    public ResponseEntity<Lesson> createLesson(
            @PathVariable @RequestParam(required = false) Long moduleId,
            @RequestBody LessonDto lessonDto) {
        Lesson createdLesson = lessonService.createLesson(moduleId, lessonDto);
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAllLessons")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getLesson/{lessonId}")
    public ResponseEntity<Lesson> getLessonByID(@PathVariable Long lessonId) {
        Optional<Lesson> lesson = lessonService.getLessonByID(lessonId);
        return lesson.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateLesson/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @RequestParam(required = false) Long moduleId,
            @RequestBody LessonDto lessonDto) {

        Lesson updatedLesson = lessonService.updateLesson(moduleId, lessonId, lessonDto);
        return ResponseEntity.ok(updatedLesson);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteLesson/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}
