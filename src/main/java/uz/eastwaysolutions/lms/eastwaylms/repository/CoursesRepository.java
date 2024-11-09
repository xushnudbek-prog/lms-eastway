package uz.eastwaysolutions.lms.eastwaylms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.eastwaysolutions.lms.eastwaylms.entity.Courses;

public interface CoursesRepository extends JpaRepository<Courses, Long> {
  }