package uz.eastwaysolutions.lms.eastwaylms.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest {
    @NotNull(message = "Phone number is mandatory")
    @NotEmpty(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotNull(message = "Password number is mandatory")
    @NotEmpty(message = "Password number is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
