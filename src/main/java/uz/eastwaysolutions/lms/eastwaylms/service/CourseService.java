package uz.eastwaysolutions.lms.eastwaylms.service;


import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;
import uz.eastwaysolutions.lms.eastwaylms.dto.courses.UpdateCourseDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {


    private final CoursesRepository coursesRepository;


    public CourseService(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    public Optional<Courses> getCourseById(Long id) {
        return coursesRepository.findById(id);
    }

    public void createCourse(CoursesDto coursesDto) {
        Courses courses = new Courses();
        courses.setTitle(coursesDto.getTitle());
        courses.setDescription(coursesDto.getDescription());
        coursesRepository.save(courses);
    }


    public Long updateCourse(Long id, UpdateCourseDto updateCourseDto) {

        Courses course = coursesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setTitle(updateCourseDto.getTitle());
        course.setDescription(updateCourseDto.getDescription());
        coursesRepository.save(course);
        return course.getId();
    }


    public void deleteCourse(Long id) {
        if (coursesRepository.existsById(id)) {
            coursesRepository.deleteById(id);
        } else {
            throw new RuntimeException("Course not found with id: " + id);
        }
    }

}
