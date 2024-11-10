package uz.eastwaysolutions.lms.eastwaylms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.modules.ModuleDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Modules;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.service.ModulesService;
import java.util.*;
@RestController
@RequestMapping("/api/modules")
public class ModulesController {


    private final ModulesService modulesService;

    public ModulesController(ModulesService modulesService) {
        this.modulesService = modulesService;
    }


    @PostMapping("/course/create/{courseId}")
    public ResponseEntity<Modules> createModule(@PathVariable @RequestParam(required = false) Long courseId,
                                                @RequestBody ModuleDto moduleDto) {
        try {
            Modules createdModule = modulesService.createModule(courseId, moduleDto);
            return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Modules>> getAllModules() {
        List<Modules> modules = modulesService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/getByID/{id}")
    public ResponseEntity<Modules> getModuleById(@PathVariable Long id) {
        Optional<Modules> module = modulesService.getModuleById(id);
        return module.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
    }

    @PutMapping("/updateModule/{moduleId}")
    public ResponseEntity<Modules> updateModule(
            @PathVariable Long moduleId,
            @RequestParam(required = false) Long courseId,
            @RequestBody ModuleDto moduleDto) {

        Modules updatedModule = modulesService.updateModule(courseId, moduleId, moduleDto);
        return ResponseEntity.ok(updatedModule);
    }


    @DeleteMapping("/deleteModule/{id}")
    public ResponseEntity<String> deleteModule(@PathVariable Long id) {
        modulesService.deleteModule(id);
        return new ResponseEntity<>("Module deleted successfully", HttpStatus.OK);
    }

}
