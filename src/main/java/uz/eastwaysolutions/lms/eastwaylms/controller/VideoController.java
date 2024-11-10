package uz.eastwaysolutions.lms.eastwaylms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.dto.video.VideoDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;
import uz.eastwaysolutions.lms.eastwaylms.service.VideoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/{lessonId}")
    public ResponseEntity<Video> createVideo(
            @PathVariable @RequestParam(required = false) Long lessonId,
            @RequestBody VideoDto videoDto) {
        Video createdVideo = videoService.createVideo(lessonId, videoDto);
        return new ResponseEntity<>(createdVideo, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/all")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{videoId}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long videoId) {
        Optional<Video> video = videoService.getVideoById(videoId);
        return video.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{videoId}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long videoId,
            @RequestParam(required = false) Long lessonId,
            @RequestBody VideoDto videoDto) {

        Video updatedVideo = videoService.updateVideo(videoId, lessonId, videoDto);
        return ResponseEntity.ok(updatedVideo);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }
}
