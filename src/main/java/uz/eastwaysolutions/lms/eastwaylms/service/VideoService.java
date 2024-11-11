package uz.eastwaysolutions.lms.eastwaylms.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.eastwaysolutions.lms.eastwaylms.dto.video.VideoDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Lesson;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;
import uz.eastwaysolutions.lms.eastwaylms.exception.ResourceNotFoundException;
import uz.eastwaysolutions.lms.eastwaylms.repository.LessonRepository;
import uz.eastwaysolutions.lms.eastwaylms.repository.VideoRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;
    private final S3Service s3Service;


    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public VideoService(VideoRepository videoRepository, LessonRepository lessonRepository, S3Service s3Service) {
        this.videoRepository = videoRepository;
        this.lessonRepository = lessonRepository;
        this.s3Service = s3Service;
    }

    public Video createVideo(Long lessonId, VideoDto videoDto, MultipartFile file) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));

        String s3Url = uploadFileToS3(file);


        Video video = Video.builder()
                .title(videoDto.getTitle())
                .duration(videoDto.getDuration())
                .url(s3Url)
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

    public Video updateVideo(Long videoId, Long lessonId, VideoDto videoDto, MultipartFile file) {
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

        if (file != null && !file.isEmpty()) {
            String s3Url = uploadFileToS3(file);
            video.setUrl(s3Url);
        }

        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Video not found with id: " + id);
        }
    }

    private String uploadFileToS3(MultipartFile file) {
        try {
            String key = "videos/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            return s3Service.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public InputStream downloadVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

        String fileKey = video.getUrl().replace("https://" + bucketName + ".s3.amazonaws.com/", "");
        return s3Service.downloadFile(fileKey);
    }


}
