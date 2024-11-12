package uz.eastwaysolutions.lms.eastwaylms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.eastwaysolutions.lms.eastwaylms.dto.video.VideoDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.Video;
import uz.eastwaysolutions.lms.eastwaylms.service.VideoService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(summary = "Create a Video", description = "Uploads a video and associates it with a specific lesson. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Video created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create/{lessonId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createVideo(
            @Parameter(description = "ID of the lesson to associate the video with", required = true) @PathVariable Long lessonId,
            @Parameter(description = "Title of the video") @RequestParam String title,
            @Parameter(description = "Duration of the video in format HH:mm:ss") @RequestParam String duration,
            @Parameter(description = "Video file to upload", required = true) @RequestPart("file") MultipartFile file) {

        VideoDto videoDto = new VideoDto(title, duration);
        Video video = videoService.createVideo(lessonId, videoDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body("Video created successfully with ID: " + video.getId());
    }


    @Operation(summary = "Get All Videos", description = "Retrieves a list of all videos. Accessible by Admin and User roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of videos retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }


    @Operation(summary = "Get Video by ID", description = "Retrieves a video by its ID. Accessible by Admin and User roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getVideoById/{videoId}")
    public ResponseEntity<Video> getVideoById(
            @Parameter(description = "ID of the video to retrieve", required = true) @PathVariable Long videoId) {
        Optional<Video> video = videoService.getVideoById(videoId);
        return video.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
    }


    @Operation(summary = "Update a Video", description = "Updates video information and optionally replaces the video file. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video updated successfully"),
            @ApiResponse(responseCode = "404", description = "Video or lesson not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{videoId}/{lessonId}")
    public ResponseEntity<Video> updateVideo(
            @Parameter(description = "ID of the video to update", required = true) @PathVariable Long videoId,
            @Parameter(description = "ID of the lesson to associate the video with", required = true) @PathVariable Long lessonId,
            @Parameter(description = "Updated title of the video") @RequestParam String title,
            @Parameter(description = "Updated duration of the video") @RequestParam String duration,
            @Parameter(description = "New video file", required = false) @RequestPart(value = "file", required = false) MultipartFile file) {

        VideoDto videoDto = new VideoDto(title, duration);
        Video video = videoService.updateVideo(videoId, lessonId, videoDto, file);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }


    @Operation(summary = "Delete a Video", description = "Deletes a video by its ID. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Video deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<Void> deleteVideo(
            @Parameter(description = "ID of the video to delete", required = true) @PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Download Video", description = "Downloads the video file. Accessible by Admin and User roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video file downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/download/{videoId}")
    public ResponseEntity<byte[]> downloadVideo(
            @Parameter(description = "ID of the video to download", required = true) @PathVariable Long videoId) throws IOException {
        InputStream videoInputStream = videoService.downloadVideo(videoId);
        byte[] videoBytes = videoInputStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoBytes.length));

        return new ResponseEntity<>(videoBytes, headers, HttpStatus.OK);
    }
}
