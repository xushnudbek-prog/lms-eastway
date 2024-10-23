package uz.eastwaysolutions.lms.eastwaylms.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.eastwaysolutions.lms.eastwaylms.utils.password.PasswordMatches;
import uz.eastwaysolutions.lms.eastwaylms.utils.password.StrongPassword;

@Getter
@Setter
@Builder
@PasswordMatches
public class RegistrationRequest {
    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstName;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastName;
    @NotEmpty(message = "Phone number is mandatory")
    @NotNull(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    @StrongPassword
    private String password;
    @NotEmpty(message = "Password Confirmation is mandatory")
    @NotNull(message = "Password Confirmation is mandatory")
    private String confirmPassword;
}
