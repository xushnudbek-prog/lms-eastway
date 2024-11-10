package uz.eastwaysolutions.lms.eastwaylms.service;

import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.lesson.LessonDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Lesson;
import uz.eastwaysolutions.lms.eastwaylms.entity.Modules;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.LessonRepository;
import uz.eastwaysolutions.lms.eastwaylms.repository.ModulesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {


    private final LessonRepository lessonRepository;
    private final ModulesRepository modulesRepository;

    public LessonService(LessonRepository lessonRepository, ModulesRepository modulesRepository) {
        this.lessonRepository = lessonRepository;
        this.modulesRepository = modulesRepository;
    }


    public Lesson createLesson(Long moduleID, LessonDto lessonDto) {

        Modules modules = modulesRepository.findById(moduleID).orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleID));
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setModule(modules);

        return lessonRepository.save(lesson);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLessonByID(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson updateLesson(Long moduleID, Long lessonID, LessonDto lessonDto) {
        if (lessonID == null) {
            throw new IllegalArgumentException("The lesson ID must not be null");
        }

        Lesson lesson = lessonRepository.findById(lessonID).orElseThrow
                (() -> new ResourceNotFoundException("Lesson not found with id: " + lessonID));


        if (moduleID != null) {
            Modules modules = modulesRepository.findById(moduleID)
                    .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleID));
            lesson.setModule(modules);
        }

        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());

        return lessonRepository.save(lesson);
    }


    public void deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        } else {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }
}
