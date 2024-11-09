package uz.eastwaysolutions.lms.eastwaylms.service;


import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.courses.CoursesDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;
import uz.eastwaysolutions.lms.eastwaylms.repository.CoursesRepository;

@Service
public class CourseService {


    private final CoursesRepository coursesRepository;

    public CourseService(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public void createCourse (CoursesDto coursesDto){
        Courses courses = new Courses();
        courses.setTitle(coursesDto.getTitle());
        courses.setDescription(coursesDto.getDescription());
        coursesRepository.save(courses);
    }


}
