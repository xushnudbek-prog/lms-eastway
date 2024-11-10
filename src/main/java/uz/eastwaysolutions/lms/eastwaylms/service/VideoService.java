package uz.eastwaysolutions.lms.eastwaylms.service;

import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.dto.video.VideoDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Lesson;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.LessonRepository;
import uz.eastwaysolutions.lms.eastwaylms.repository.VideoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    public VideoService(VideoRepository videoRepository, LessonRepository lessonRepository) {
        this.videoRepository = videoRepository;
        this.lessonRepository = lessonRepository;
    }

    public Video createVideo(Long lessonId, VideoDto videoDto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));

        Video video = Video.builder()
                .title(videoDto.getTitle())
                .duration(videoDto.getDuration())
                .url(videoDto.getUrl())
                .lesson(lesson)
                .build();

        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }


    public Video updateVideo(Long videoId, Long lessonId, VideoDto videoDto) {
        if (videoId == null) {
            throw new IllegalArgumentException("The video ID must not be null");
        }

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

        if (lessonId != null) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
            video.setLesson(lesson);
        }

        video.setTitle(videoDto.getTitle());
        video.setDuration(videoDto.getDuration());
        video.setUrl(videoDto.getUrl());

        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Video not found with id: " + id);
        }
    }
}
