package uz.eastwaysolutions.lms.eastwaylms.dto.user;

import lombok.*;
import uz.eastwaysolutions.lms.eastwaylms.entity.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
}
