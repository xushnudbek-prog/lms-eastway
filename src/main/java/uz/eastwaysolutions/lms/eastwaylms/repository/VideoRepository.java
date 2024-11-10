package uz.eastwaysolutions.lms.eastwaylms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}