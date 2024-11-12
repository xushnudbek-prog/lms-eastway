package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.modules.ModuleDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Modules;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.service.ModulesService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
public class ModulesController {

    private final ModulesService modulesService;

    public ModulesController(ModulesService modulesService) {
        this.modulesService = modulesService;
    }

    @Operation(summary = "Create a new module", description = "Creates a module for a specified course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Module created successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/course/create/{courseId}")
    public ResponseEntity<Modules> createModule(
            @Parameter(description = "ID of the course to which the module belongs", required = true) @PathVariable Long courseId,
            @RequestBody ModuleDto moduleDto) {
        try {
            Modules createdModule = modulesService.createModule(courseId, moduleDto);
            return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Get all modules", description = "Retrieves a list of all modules.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of modules")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<Modules>> getAllModules() {
        List<Modules> modules = modulesService.getAllModules();
        return ResponseEntity.ok(modules);
    }


    @Operation(summary = "Get module by ID", description = "Retrieves a module by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the module"),
            @ApiResponse(responseCode = "404", description = "Module not found", content = @Content)
    })
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Modules> getModuleById(
            @Parameter(description = "ID of the module to retrieve", required = true) @PathVariable Long id) {
        Optional<Modules> module = modulesService.getModuleById(id);
        return module.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
    }


    @Operation(summary = "Update a module", description = "Updates an existing module by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module updated successfully"),
            @ApiResponse(responseCode = "404", description = "Module not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PutMapping("/updateModule/{moduleId}")
    public ResponseEntity<Modules> updateModule(
            @Parameter(description = "ID of the module to update", required = true) @PathVariable Long moduleId,
            @Parameter(description = "ID of the course to which the module belongs (optional)") @RequestParam(required = false) Long courseId,
            @RequestBody ModuleDto moduleDto) {

        Modules updatedModule = modulesService.updateModule(courseId, moduleId, moduleDto);
        return ResponseEntity.ok(updatedModule);
    }


    @Operation(summary = "Delete a module", description = "Deletes a module by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Module not found", content = @Content)
    })
    @DeleteMapping("/deleteModule/{id}")
    public ResponseEntity<String> deleteModule(
            @Parameter(description = "ID of the module to delete", required = true) @PathVariable Long id) {
        modulesService.deleteModule(id);
        return new ResponseEntity<>("Module deleted successfully", HttpStatus.OK);
    }
}
