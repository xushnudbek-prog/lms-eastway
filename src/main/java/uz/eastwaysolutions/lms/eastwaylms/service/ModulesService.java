package uz.eastwaysolutions.lms.eastwaylms.service;

import uz.eastwaysolutions.lms.eastwaylms.dto.modules.ModuleDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.entity.Modules;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;
import uz.eastwaysolutions.lms.eastwaylms.repository.ModulesRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModulesService {


    private final ModulesRepository modulesRepository;


    private final CoursesRepository coursesRepository;

    public ModulesService(ModulesRepository modulesRepository, CoursesRepository coursesRepository) {
        this.modulesRepository = modulesRepository;
        this.coursesRepository = coursesRepository;
    }


    public Modules createModule(Long courseId, ModuleDto moduleDto) {
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        Modules module = new Modules();
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());
        module.setCourse(course);

        return modulesRepository.save(module);
    }



    public List<Modules> getAllModules() {
        return modulesRepository.findAll();
    }

    public Optional<Modules> getModuleById(Long id) {
        return modulesRepository.findById(id);
    }

    public Modules updateModule(Long courseId, Long moduleId, ModuleDto moduleDto) {
        if (moduleId == null) {
            throw new IllegalArgumentException("The module ID must not be null");
        }

        Modules module = modulesRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId));

        if (courseId != null) {
            Courses course = coursesRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
            module.setCourse(course);
        }

        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());

        return modulesRepository.save(module);
    }


    public void deleteModule(Long id) {
        if (modulesRepository.existsById(id)) {
            modulesRepository.deleteById(id);
        } else {
            throw new RuntimeException("Module not found with id: " + id);
        }
    }
}
