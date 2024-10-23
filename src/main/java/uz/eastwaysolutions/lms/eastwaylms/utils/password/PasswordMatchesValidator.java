package uz.eastwaysolutions.lms.eastwaylms.utils.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.RegistrationRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return false;
        }
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
