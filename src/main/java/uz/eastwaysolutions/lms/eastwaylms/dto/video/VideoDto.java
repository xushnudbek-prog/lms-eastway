package uz.eastwaysolutions.lms.eastwaylms.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link uz.eastwaysolutions.lms.eastwaylms.entity.Video}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VideoDto implements Serializable {
    private String title;
    private String duration;
    private String url;
}