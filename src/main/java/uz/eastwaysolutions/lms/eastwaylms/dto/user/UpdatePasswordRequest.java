package uz.eastwaysolutions.lms.eastwaylms.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.eastwaysolutions.lms.eastwaylms.utils.password.PasswordMatches;
import uz.eastwaysolutions.lms.eastwaylms.utils.password.StrongPassword;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UpdatePasswordRequest {
    private String oldPassword;
    @StrongPassword
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
    @NotEmpty(message = "Password Confirmation is mandatory")
    @NotNull(message = "Password Confirmation is mandatory")
    private String confirmPassword;
}
