package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.eastwaysolutions.lms.eastwaylms.dto.video.VideoDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;
import uz.eastwaysolutions.lms.eastwaylms.service.S3Service;
import uz.eastwaysolutions.lms.eastwaylms.service.VideoService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {


    private final VideoService videoService;
    private final S3Service s3Service;

    public VideoController(VideoService videoService, S3Service s3Service) {
        this.videoService = videoService;
        this.s3Service = s3Service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create/{lessonId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createVideo(
            @PathVariable Long lessonId,
            @Parameter @RequestParam String title,
            @Parameter @RequestParam String duration,
            @RequestPart("file") MultipartFile file) {

        VideoDto videoDto = new VideoDto(title, duration);
        Video video = videoService.createVideo(lessonId, videoDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body("Video created successfully with ID: " + video.getId());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getVideoById/{videoId}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long videoId) {
        Optional<Video> video = videoService.getVideoById(videoId);
        return video.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{videoId}/{lessonId}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long videoId,
            @PathVariable Long lessonId,
            @Parameter @RequestParam String title,
            @Parameter @RequestParam String duration,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        VideoDto videoDto = new VideoDto(title, duration);
        Video video = videoService.updateVideo(videoId, lessonId, videoDto, file);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/download/{videoId}")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable Long videoId) throws IOException {
        InputStream videoInputStream = videoService.downloadVideo(videoId);

        byte[] videoBytes = videoInputStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoBytes.length));

        return new ResponseEntity<>(videoBytes, headers, HttpStatus.OK);
    }
}
