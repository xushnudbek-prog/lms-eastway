package uz.eastwaysolutions.lms.eastwaylms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.eastwaysolutions.lms.eastwaylms.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}