package uz.eastwaysolutions.lms.eastwaylms.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link uz.eastwaysolutions.lms.eastwaylms.entity.Lesson}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LessonDto implements Serializable {
    private String title;
    private String description;
}