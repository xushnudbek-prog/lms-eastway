package uz.eastwaysolutions.lms.eastwaylms.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
